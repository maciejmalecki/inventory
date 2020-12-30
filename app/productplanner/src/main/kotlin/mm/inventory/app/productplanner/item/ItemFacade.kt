package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.domain.items.ITEMS_ROLE
import mm.inventory.domain.items.ITEMS_WRITER_ROLE
import mm.inventory.domain.items.item.ItemFactory
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemMutator
import mm.inventory.domain.items.item.ItemSelector
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction
import mm.inventory.domain.shared.transactions.TransactionalHandler
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.ItemId

/**
 * Facade for Item application component.
 */
class ItemFacade(
    private val sec: SecurityGuard,
    private val tx: BusinessTransaction,
    private val itemSelector: ItemSelector,
    private val itemMutator: ItemMutator,
    private val itemQuery: ItemQuery,
    private val itemFactory: ItemFactory
) {
    fun findAllItems(): ImmutableList<ItemHeader> = sec.requireRole(ITEMS_ROLE) {
        itemQuery.findAll()
    }

    fun findById(id: ItemId): Item? = sec.requireRole(ITEMS_ROLE) {
        itemSelector.findById(id)
    }

    fun createItem(name: String, itemClassId: ItemClassId, inValues: Map<String, String>): Item =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            itemFactory.create(name, itemClassId, inValues)
        }

    fun updateItem(id: ItemId, inValues: Map<String, String>) =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            val item = itemSelector.get(id)
            itemMutator.save(item.updateValues(inValues))
        }

    fun deleteItem(id: ItemId) =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            tx.inTransaction {
                val item = itemSelector.get(id)
                itemMutator.delete(item)
            }
        }
}