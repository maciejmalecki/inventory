package mm.inventory.domain.shared.types

interface ItemId {
    fun `==`(other: ItemId): Boolean
    val empty: Boolean
        get() = false
}

val emptyItemId: ItemId = object : ItemId {
    override fun `==`(other: ItemId) = false
    override val empty: Boolean
        get() = true
}