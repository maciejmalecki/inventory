package mm.inventory.adapters.store.r2dbc

import io.r2dbc.client.Handle
import io.r2dbc.client.R2dbc
import io.r2dbc.spi.Result
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.reactive.awaitFirst
import mm.inventory.app.categories.Category
import mm.inventory.app.categories.CategoryCrudRepository
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

class CategoryCrudR2dbcRepository(private val r: R2dbc) : CategoryCrudRepository {

    override suspend fun create(code: String, name: String): Category =
             r.inTransaction {
                insertIntoCategories(it, code, name).flatMap { categoryId ->
                    insertRootIntoCategoryTreePath(it, categoryId).flatMap { _ ->
                        selectCategoryById(it, categoryId)
                    }
                }
            }.awaitFirst()


    override suspend fun create(code: String, name: String, parentId: Long): Category =
            r.inTransaction { handle ->
                insertIntoCategories(handle, code, name).flatMap { categoryId ->
                    insertRootIntoCategoryTreePath(handle, categoryId).flatMap {
                        insertIntoCategoryTreePath(handle, parentId, categoryId).flatMap {
                            selectCategoryById(handle, categoryId)
                        }
                    }
                }
            }.awaitFirst()

    override suspend fun findPathById(id: Long): ImmutableList<Category> =
            r.withHandle {
                selectPath(it, id)
            }.collectList().map {
                it.toImmutableList()
            }.awaitFirst()

    override suspend fun findAllRoot(): ImmutableSet<Category> =
            r.withHandle {
                selectCategoryWithNoParent(it)
            }.collectList().map {
                it.toImmutableSet()
            }.awaitFirst()

    override suspend fun findAll(parentId: Long): ImmutableSet<Category> =
            r.withHandle {
                selectCategoryForParent(it, parentId)
            }.collectList().map {
                it.toImmutableSet()
            }.awaitFirst()

    private fun insertIntoCategories(it: Handle, code: String, name: String): Flux<Long> {
        it.execute("INSERT INTO Categories (code, name) VALUES ($1, $2)", code, name)
        return it.select("SELECT LASTVAL()").mapResult(extractLong(1))
    }

    private fun insertRootIntoCategoryTreePath(it: Handle, categoryId: Long): Flux<Int> =
            it.execute("""INSERT INTO Categories_Tree_Path (ancestor_id, descendant_id, depth)
                            |VALUES ($1, $2, $3)""".trimMargin(), categoryId, categoryId, 0)

    private fun insertIntoCategoryTreePath(it: Handle, parentId: Long, leafId: Long): Flux<Int> =
            it.execute("""INSERT INTO Categories_Tree_Path (ancestor_id, descendant_id, depth)
                            |SELECT p.ancestor_id, c.descendant_id, p.depth + c.depth + 1 
                            |FROM Categories_Tree_Path p, Categories_Tree_Path c
                            |WHERE p.descendant_id=$1 AND c.ancestor_id=$2""".trimMargin(), parentId, leafId)

    private fun extractLong(param: Int): (Result) -> Publisher<Long> = { result: Result ->
        result.map { row, _ -> row.get(param, Long::class.java) }
    }

    private fun selectCategoryById(it: Handle, id: Long): Flux<Category> =
            it.select("SELECT category_id, code, name FROM Categories WHERE category_id=$1", id).mapResult { result ->
                result.map(::categoryMapper)
            }

    private fun selectPath(it: Handle, leafId: Long): Flux<Category> =
            it.select("""SELECT category_id, code, name 
                           |FROM Categories c JOIN Categories_Tree_path t ON c.category_id=t.descendant_id
                           |WHERE t.descendant_id = $1
                           |ORDER BY t.depth DESC""".trimMargin(), leafId).mapResult { result ->
                result.map(::categoryMapper)
            }

    private fun selectCategoryWithNoParent(it: Handle): Flux<Category> =
            it.select("""SELECT category_id, code, name
                           |FROM Categories c JOIN Categories_Tree_Path t ON c.category_id=t.descendant_id
                           |WHERE t.depth = 0""".trimMargin()).mapResult { result ->
                result.map(::categoryMapper)
            }

    private fun selectCategoryForParent(it: Handle, parentId: Long): Flux<Category> =
            it.select("""SELECT category_id, code, name
                           |FROM Categories c JOIN Categories_Tree_Path t ON c.category_id=t.descendant_id
                           |WHERE t.ancestor_id = $1""".trimMargin(), parentId).mapResult { result ->
                result.map(::categoryMapper)
            }
}
