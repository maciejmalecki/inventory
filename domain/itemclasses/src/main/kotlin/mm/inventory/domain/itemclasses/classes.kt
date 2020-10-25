package mm.inventory.domain.itemclasses


data class ItemClass(
        val name: String,
        val description: String,
        val amountUnit: UnitOfMeasurement)

data class ItemClassVersion(
        val itemClass: ItemClass,
        val version: Int)


