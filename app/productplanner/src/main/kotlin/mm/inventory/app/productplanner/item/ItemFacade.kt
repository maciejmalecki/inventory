package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.domain.items.ITEMS_ROLE
import mm.inventory.domain.items.ITEMS_WRITER_ROLE
import mm.inventory.domain.items.behaviors.CreateItem
import mm.inventory.domain.items.behaviors.UpdateItem
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemMutator
import mm.inventory.domain.items.item.ItemSelector
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.ItemId

/**
 * Facade for Item application component.
 */
class ItemFacade(
    private val sec: SecurityGuard,
    private val itemSelector: ItemSelector,
    private val itemMutator: ItemMutator,
    private val itemQuery: ItemQuery,
    private val createItem: CreateItem,
    private val updateItem: UpdateItem
) {
    fun findAllItems(): ImmutableList<ItemHeader> = sec.requireRole(ITEMS_ROLE) {
        itemQuery.findAll()
    }

    fun findById(id: ItemId): Item? = sec.requireRole(ITEMS_ROLE) {
        itemSelector.findById(id)
    }

    fun createItem(name: String, itemClassId: ItemClassId, inValues: Map<String, String>): Item =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            createItem.execute(name, itemClassId, inValues)
        }

    fun updateItem(id: ItemId, inValues: Map<String, String>) =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            updateItem.execute(id, inValues)
        }

    fun deleteItem(id: ItemId) =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            // TODO maybe this will be a separate behavior
            val item = itemSelector.get(id)
            itemMutator.delete(item)
        }
}