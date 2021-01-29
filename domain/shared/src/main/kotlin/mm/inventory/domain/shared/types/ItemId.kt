package mm.inventory.domain.shared.types

interface ItemId {
    val empty: Boolean
        get() = false
}

val emptyItemId: ItemId = object : ItemId {
    override val empty = true
    override fun equals(other: Any?): Boolean = false
}