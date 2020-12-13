package mm.inventory.app.productplanner.itemclass

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.domain.items.ItemClass
import mm.inventory.domain.items.ItemClassSelector

class ItemClassFacade(
    private val itemClassSelector: ItemClassSelector,
    private val itemClassQuery: ItemClassQuery
) {
    fun findAll(): ImmutableList<ItemClassHeader> = itemClassQuery.findAll()
    fun findByName(name: String): ItemClass? = itemClassSelector.findByName(name)
}