package mm.inventory.adapters.store.r2dbc

import io.r2dbc.client.Handle
import io.r2dbc.client.R2dbc
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.reactive.awaitSingle
import mm.inventory.app.categories.Category
import mm.inventory.app.categories.CategoryCrudRepository
import mm.inventory.app.categories.CategoryPath
import reactor.core.publisher.Flux

class CategoryCrudR2dbcRepository(private val db: R2dbc) : CategoryCrudRepository {

    override suspend fun create(code: String, name: String): Category =
            db.inTransaction {
                insertCategory(it, code, name).flatMap { categoryId ->
                    insertRootIntoCategoryTreePath(it, categoryId)
                            .thenMany(selectCategoryById(it, categoryId))
                }
            }.awaitSingle()

    override suspend fun create(code: String, name: String, parentId: Long): Category =
            db.inTransaction {
                insertCategory(it, code, name).flatMap { categoryId ->
                    insertRootIntoCategoryTreePath(it, categoryId)
                            .thenMany(insertIntoCategoryTreePath(it, parentId, categoryId))
                            .thenMany(selectCategoryById(it, categoryId))
                }
            }.awaitSingle()

    override suspend fun delete(id: Long): Int =
            db.inTransaction {
                deleteSubtree(it, id).thenMany(deleteFreeNodes(it))
            }.awaitSingle()

    override suspend fun findPathById(id: Long): ImmutableList<Category> =
            db.withHandle {
                selectPath(it, id)
            }.collectList().map {
                it.toImmutableList()
            }.awaitSingle()

    override suspend fun findAllRoot(): ImmutableSet<Category> =
            db.withHandle {
                selectCategoriesWithNoParent(it)
            }.collectList().map {
                it.toImmutableSet()
            }.awaitSingle()

    override suspend fun findAll(parentId: Long): ImmutableSet<Category> =
            db.withHandle {
                selectCategoriesForParent(it, parentId)
            }.collectList().map {
                it.toImmutableSet()
            }.awaitSingle()

    override suspend fun findAllPathNames(separator: String): ImmutableList<CategoryPath> =
            db.withHandle {
                selectAllCategoryPaths(it, separator)
            }.collectList().map {
                it.toImmutableList()
            }.awaitSingle()

    private fun insertCategory(it: Handle, code: String, name: String): Flux<Long> =
            it.execute("INSERT INTO Categories (code, name) VALUES ($1, $2)", code, name).flatMap { _ ->
                it.select("SELECT LASTVAL()").mapRow { row ->
                    row.get(0) as Long
                }
            }

    private fun insertRootIntoCategoryTreePath(it: Handle, categoryId: Long): Flux<Int> =
            it.execute("""INSERT INTO Categories_Tree_Path (ancestor_id, descendant_id, depth)
                            |VALUES ($1, $2, $3)""".trimMargin(), categoryId, categoryId, 0)

    private fun insertIntoCategoryTreePath(it: Handle, parentId: Long, leafId: Long): Flux<Int> =
            it.execute("""INSERT INTO Categories_Tree_Path (ancestor_id, descendant_id, depth)
                            |SELECT p.ancestor_id, c.descendant_id, p.depth + c.depth + 1 
                            |FROM Categories_Tree_Path p, Categories_Tree_Path c
                            |WHERE p.descendant_id=$1 AND c.ancestor_id=$2""".trimMargin(), parentId, leafId)

    private fun deleteSubtree(it: Handle, id: Long): Flux<Int> =
            it.execute("""DELETE FROM Categories_Tree_Path ctp
                            |WHERE ctp.descendant_id IN
                               |(SELECT ctp2.descendant_id FROM Categories_Tree_Path ctp2 
                                   |WHERE ctp2.ancestor_id=$1)""".trimMargin(), id)

    private fun deleteFreeNodes(it: Handle): Flux<Int> =
            it.execute("""DELETE FROM Categories 
                            |WHERE category_id NOT IN 
                               |(SELECT ancestor_id FROM Categories_Tree_Path
                                    |UNION
                                |SELECT descendant_id FROM Categories_Tree_Path)""".trimMargin())

    private fun selectCategoryById(it: Handle, id: Long): Flux<Category> =
            it.select("SELECT category_id, code, name FROM Categories WHERE category_id=$1", id)
                    .mapRow(::categoryMapper)

    private fun selectPath(it: Handle, leafId: Long): Flux<Category> =
            it.select("""SELECT category_id, code, name 
                           |FROM Categories c JOIN Categories_Tree_path t ON c.category_id=t.ancestor_id
                           |WHERE t.descendant_id = $1
                           |ORDER BY t.depth DESC""".trimMargin(), leafId).mapRow(::categoryMapper)

    private fun selectCategoriesWithNoParent(it: Handle): Flux<Category> =
            it.select("""SELECT category_id, code, name
                           |FROM Categories c JOIN Categories_Tree_Path t ON c.category_id=t.descendant_id
                           |WHERE t.depth = $1 AND t.ancestor_id NOT IN
                             |(SELECT descendant_id FROM Categories_Tree_Path WHERE depth=$2)""".trimMargin(), 0, 1).mapRow(::categoryMapper)

    private fun selectCategoriesForParent(it: Handle, parentId: Long): Flux<Category> =
            it.select("""SELECT category_id, code, name
                           |FROM Categories c JOIN Categories_Tree_Path t ON c.category_id=t.descendant_id
                           |WHERE t.ancestor_id = $1 AND t.depth = $2""".trimMargin(), parentId, 1).mapRow(::categoryMapper)

    private fun selectAllCategoryPaths(it: Handle, separator: String): Flux<CategoryPath> =
            it.select("""WITH cat AS (
                           |SELECT DISTINCT c.category_id AS category_id, cc.code AS code, c2.depth AS depth
                           |FROM Categories c
                           |JOIN categories_tree_path c1 ON c1.descendant_id = c.category_id
                           |JOIN categories_tree_path c2 ON c2.descendant_id = c1.descendant_id
                           |JOIN Categories cc ON c2.ancestor_id = cc.category_id)
                        |SELECT category_id, STRING_AGG(CAST(code AS TEXT), $1 ORDER BY depth DESC) AS path
                        |FROM cat
                        |GROUP BY category_id
                        |ORDER BY path""".trimMargin(), separator).mapRow { row ->
                CategoryPath(row.get("category_id") as Long, row.get("path") as String)
            }
}