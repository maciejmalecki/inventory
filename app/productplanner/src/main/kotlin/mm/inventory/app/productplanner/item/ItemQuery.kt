package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.domain.shared.types.ItemId

data class ItemHeader(val id: ItemId, val name: String, val itemClassName: String)

interface ItemQuery {
    fun findAll(): ImmutableList<ItemHeader>
}