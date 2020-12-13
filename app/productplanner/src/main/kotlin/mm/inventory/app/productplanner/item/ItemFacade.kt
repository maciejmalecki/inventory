package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableMap
import mm.inventory.domain.items.ItemSelector
import mm.inventory.domain.items.behaviors.CreateItem
import mm.inventory.domain.items.behaviors.UpdateItem

class ItemFacade(
    private val itemSelector: ItemSelector,
    private val itemQuery: ItemQuery,
    private val createItem: CreateItem,
    private val updateItem: UpdateItem
) {
    fun findAllItems() = itemQuery.findAll()

    fun findByName(name: String) = itemSelector.findByName(name)

    fun createItem(name: String, itemClassName: String, inValues: ImmutableMap<String, String>) =
        createItem.execute(name, itemClassName, inValues)

    fun updateItem(name: String, inValues: ImmutableMap<String, String>) =
        updateItem.execute(name, inValues)
}