package mm.inventory.app.productplanner.itemclass

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.domain.shared.types.ItemClassId

data class ItemClassHeader(val id: ItemClassId, val name: String, val description: String)

interface ItemClassQuery {
    fun findAll(): ImmutableList<ItemClassHeader>
}