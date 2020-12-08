package mm.inventory.app.itemsfacade.item

import kotlinx.collections.immutable.ImmutableMap
import mm.inventory.domain.items.CreateItem
import mm.inventory.domain.items.ItemRepository

class ItemFacade(
    private val itemRepository: ItemRepository,
    private val itemCrudRepository: ItemCrudRepository,
    private val createItem: CreateItem
) {
    fun findAllItems() = itemCrudRepository.selectItems()

    fun findByName(name: String) = itemRepository.findByName(name)

    fun createItem(name: String, itemClassName: String, inValues: ImmutableMap<String, String>) =
        createItem.execute(name, itemClassName, inValues)
}