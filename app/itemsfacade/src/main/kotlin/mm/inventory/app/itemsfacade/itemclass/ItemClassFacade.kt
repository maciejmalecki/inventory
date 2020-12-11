package mm.inventory.app.itemsfacade.itemclass

import mm.inventory.domain.items.ItemClassRepository

class ItemClassFacade(private val itemClassRepository: ItemClassRepository) {
    fun findByName(name: String) = itemClassRepository.findByName(name)
}