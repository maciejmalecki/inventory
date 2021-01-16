package mm.inventory.domain.shared.types

interface ManufacturerId {
    val empty: Boolean
        get() = false
}

val emptyManufacturerId: ManufacturerId = object : ManufacturerId {
    override val empty = true
    override fun equals(other: Any?) = false
}