package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableList

data class ItemHeader(val name: String, val itemClassName: String)

interface ItemQuery {
    fun findAll(): ImmutableList<ItemHeader>
}