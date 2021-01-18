package mm.inventory.domain.shared.types

interface ItemClassId {
    val empty: Boolean
        get() = false
}

val emptyItemClassId: ItemClassId = object : ItemClassId {
    override val empty = true
    override fun equals(other: Any?) = false
}