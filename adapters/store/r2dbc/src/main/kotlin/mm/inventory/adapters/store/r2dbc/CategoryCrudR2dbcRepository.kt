package mm.inventory.adapters.store.r2dbc

import io.r2dbc.client.Handle
import io.r2dbc.client.R2dbc
import io.r2dbc.spi.Result
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.reactive.awaitFirst
import mm.inventory.app.categories.Category
import mm.inventory.app.categories.CategoryCrudRepository
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class CategoryCrudR2dbcRepository(private val r: R2dbc) : CategoryCrudRepository {

    override suspend fun create(code: String, name: String): Category = r.inTransaction {
        insertIntoCategories(it, code, name).flatMap { categoryId ->
            insertIntoCategoryTreePath(it, categoryId, categoryId, 0).flatMap { _ ->
                selectCategoryById(it, categoryId)
            }
        }
    }.awaitFirst()


    override suspend fun create(code: String, name: String, parent: Category): Category = r.inTransaction {
        insertIntoCategories(it, code, name).flatMap { categoryId ->
            selectMaxDepth(it, parent.id).flatMap { depth ->
                insertIntoCategoryTreePath(it, parent.id, categoryId, depth + 1).flatMap { _ ->
                    selectCategoryById(it, categoryId)
                }
            }
        }
    }.awaitFirst()

    override suspend fun findAllRoot(): ImmutableSet<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(parent: Category): ImmutableSet<Category> {
        TODO("Not yet implemented")
    }

    private fun insertIntoCategories(it: Handle, code: String, name: String): Flux<Long> {
        it.execute("INSERT INTO Categories (code, name) VALUES ($1, $2)", code, name)
        return it.select("SELECT LASTVAL()").mapResult(extractLong(1))
    }

    private fun insertIntoCategoryTreePath(it: Handle, ancestorId: Long, descendantId: Long, depth: Int): Flux<Int> =
            it.execute("INSERT INTO Categories_Tree_Path (ancestor_id, descendant_id, depth) VALUES ($1, $2, $3)", ancestorId, descendantId, depth)

    private fun selectMaxDepth(it: Handle, categoryId: Long): Flux<Int> =
            it.select("SELECT MAX(depth) FROM Categories_Tree_Path WHERE descendant_id=$1", categoryId)
                    .mapResult(extractInt(1)).or(Mono.just(0))

    private fun extractLong(param: Int): (Result) -> Publisher<Long> = { result: Result ->
        result.map { row, _ -> row.get(param, Long::class.java) }
    }

    private fun extractInt(param: Int): (Result) -> Publisher<Int> = { result: Result ->
        result.map { row, _ -> row.get(param, Int::class.java) }
    }

    private fun selectCategoryById(it: Handle, id: Long): Flux<Category> =
            it.select("SELECT category_id, code, name FROM Categories WHERE category_id=$1", id).mapResult { result ->
                result.map { row, _ ->
                    Category(
                            row.get("category_id", Long::class.java)!!,
                            row.get("code", String::class.java)!!,
                            row.get("name", String::class.java)!!)
                }
            }

}
