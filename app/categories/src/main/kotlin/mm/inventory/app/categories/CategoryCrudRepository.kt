package mm.inventory.app.categories

import kotlinx.collections.immutable.ImmutableSet

interface CategoryCrudRepository {
    suspend fun create(code: String, name: String): Category
    suspend fun create(code: String, name: String, parent: Category): Category
    suspend fun findAllRoot(): ImmutableSet<Category>
    suspend fun findAll(parent: Category): ImmutableSet<Category>
}