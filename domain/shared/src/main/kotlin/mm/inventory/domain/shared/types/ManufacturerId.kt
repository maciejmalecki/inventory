package mm.inventory.domain.shared.types

interface ManufacturerId {
    val empty: Boolean
        get() = false
}

val emptyManufacturerId: ManufacturerId = object : ManufacturerId {
    override fun equals(other: Any?) = false
    override val empty: Boolean
        get() = true
}