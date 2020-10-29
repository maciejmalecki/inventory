package mm.inventory.app.categories

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

interface CategoryCrudRepository {
    suspend fun create(code: String, name: String): Category
    suspend fun create(code: String, name: String, parentId: Long): Category
    suspend fun delete(id: Long): Int
    suspend fun findPathById(id: Long): ImmutableList<Category>
    suspend fun findAllRoot(): ImmutableSet<Category>
    suspend fun findAll(parentId: Long): ImmutableSet<Category>
    suspend fun findAllPathNames(separator: String = "-"): ImmutableList<CategoryPath>
}