package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import mm.inventory.domain.items.ITEMS_ROLE
import mm.inventory.domain.items.ITEMS_WRITER_ROLE
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemSelector
import mm.inventory.domain.items.behaviors.CreateItem
import mm.inventory.domain.items.behaviors.UpdateItem
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.ItemId

/**
 * Facade for Item application component.
 */
class ItemFacade(
    private val sec: SecurityGuard,
    private val itemSelector: ItemSelector,
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

    fun createItem(name: String, itemClassId: ItemClassId, inValues: ImmutableMap<String, String>): Item =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            createItem.execute(name, itemClassId, inValues)
        }

    fun updateItem(id: ItemId, inValues: ImmutableMap<String, String>) =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            updateItem.execute(id, inValues)
        }
}