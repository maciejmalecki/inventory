package mm.inventory.app.itemsfacade.itemclass

import mm.inventory.domain.items.ItemClassSelector

class ItemClassFacade(
    private val itemClassSelector: ItemClassSelector,
    private val itemClassQuery: ItemClassQuery
) {
    fun findAll() = itemClassQuery.findAll()
    fun findByName(name: String) = itemClassSelector.findByName(name)
}