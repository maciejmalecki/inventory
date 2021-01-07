package mm.inventory.domain.shared.types

interface ItemClassId {
    val empty: Boolean
        get() = false
}

val emptyItemClassId: ItemClassId = object : ItemClassId {
    override fun equals(other: Any?) = false
    override val empty: Boolean
        get() = true
}