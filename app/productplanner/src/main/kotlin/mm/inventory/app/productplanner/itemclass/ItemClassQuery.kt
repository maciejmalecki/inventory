package mm.inventory.app.productplanner.itemclass

import kotlinx.collections.immutable.ImmutableList

data class ItemClassHeader(val name: String, val description: String)

interface ItemClassQuery {
    fun findAll(): ImmutableList<ItemClassHeader>
}