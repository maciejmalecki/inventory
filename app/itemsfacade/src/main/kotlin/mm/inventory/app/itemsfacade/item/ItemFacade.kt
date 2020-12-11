package mm.inventory.app.itemsfacade.item

import kotlinx.collections.immutable.ImmutableMap
import mm.inventory.domain.items.behaviors.CreateItem
import mm.inventory.domain.items.ItemSelector

class ItemFacade(
    private val itemSelector: ItemSelector,
    private val itemCrudRepository: ItemCrudRepository,
    private val createItem: CreateItem
) {
    fun findAllItems() = itemCrudRepository.selectItems()

    fun findByName(name: String) = itemSelector.findByName(name)

    fun createItem(name: String, itemClassName: String, inValues: ImmutableMap<String, String>) =
        createItem.execute(name, itemClassName, inValues)
}