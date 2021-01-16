package mm.inventory.domain.shared.types

interface ItemClassId {
    val version: Long
    val empty: Boolean
        get() = false
}

val emptyItemClassId: ItemClassId = object : ItemClassId {
    override val version = -1L
    override val empty = true
    override fun equals(other: Any?) = false
}