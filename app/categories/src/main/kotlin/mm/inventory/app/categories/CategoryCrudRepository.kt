package mm.inventory.app.categories

interface CategoryCrudRepository {
    suspend fun create(code: String, name: String): Category
    suspend fun create(code: String, name: String, parentId: Long): Category
    suspend fun delete(id: Long): Int
    suspend fun findPathById(id: Long): List<Category>
    suspend fun findAllRoot(): Set<Category>
    suspend fun findAll(parentId: Long): Set<Category>
    suspend fun findAllPathNames(separator: String = "-"): List<CategoryPath>
}