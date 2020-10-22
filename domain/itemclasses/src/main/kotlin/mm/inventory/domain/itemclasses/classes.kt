package mm.inventory.domain.itemclasses

data class Category(
        val name: String,
        val description: String,
        val parent: Category?
)

val ROOT_CATEGORY = Category("root", "", null)

data class ItemClass(
        val name: String,
        val description: String,
        val amountUnit: UnitOfMeasurement)

data class ItemClassVersion(
        val itemClass: ItemClass,
        val version: Int)


