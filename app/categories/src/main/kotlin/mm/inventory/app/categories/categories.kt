package mm.inventory.app.categories

data class Category(
        val id: Long,
        val code: String,
        val name: String
)

data class CategoryPath(
        val leafId: Long,
        val path: String
)

data class CategorySection(
        val code: String,
        val name: String
)