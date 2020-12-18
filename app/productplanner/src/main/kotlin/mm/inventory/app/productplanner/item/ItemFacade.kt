package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemSelector
import mm.inventory.domain.items.behaviors.CreateItem
import mm.inventory.domain.items.behaviors.UpdateItem
import mm.inventory.domain.shared.types.ItemId

/**
 * Facade for Item application component.
 */
class ItemFacade(
    private val itemSelector: ItemSelector,
    private val itemQuery: ItemQuery,
    private val createItem: CreateItem,
    private val updateItem: UpdateItem
) {
    fun findAllItems(): ImmutableList<ItemHeader> = itemQuery.findAll()
    fun findById(id: ItemId): Item? = itemSelector.findById(id)

    fun createItem(name: String, itemClassName: String, inValues: ImmutableMap<String, String>): Item =
        createItem.execute(name, itemClassName, inValues)

    fun updateItem(id: ItemId, inValues: ImmutableMap<String, String>) =
        updateItem.execute(id, inValues)
}