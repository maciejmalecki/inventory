package mm.inventory.app.itemsfacade.item

import mm.inventory.domain.items.ItemRepository

class ItemFacade(private val itemRepository: ItemRepository, private val itemCrudRepository: ItemCrudRepository) {
    fun findAllItems() = itemCrudRepository.selectItems()
    fun findByName(name: String) = itemRepository.findByName(name)
}