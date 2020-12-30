package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.domain.items.ITEMS_ROLE
import mm.inventory.domain.items.ITEMS_WRITER_ROLE
import mm.inventory.domain.items.item.ItemFactory
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemRepository
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.ItemId

/**
 * Facade for Item application component.
 */
class ItemFacade(
    private val sec: SecurityGuard,
    private val tx: BusinessTransaction,
    private val itemRepository: ItemRepository,
    private val itemQuery: ItemQuery,
    private val itemFactory: ItemFactory
) {
    fun findAllItems(): ImmutableList<ItemHeader> = sec.requireRole(ITEMS_ROLE) {
        itemQuery.findAll()
    }

    fun findById(id: ItemId): Item? = sec.requireRole(ITEMS_ROLE) {
        itemRepository.findById(id)
    }

    fun createItem(name: String, itemClassId: ItemClassId, inValues: Map<String, String>): Item =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            itemFactory.create(name, itemClassId, inValues)
        }

    fun updateItem(id: ItemId, inValues: Map<String, String>) =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            tx.inTransaction {
                val item = itemRepository.get(id)
                val updatedItem = item.updateValues(inValues);
                itemRepository.save(updatedItem)
            }
        }

    fun deleteItem(id: ItemId) =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            tx.inTransaction {
                val item = itemRepository.get(id)
                itemRepository.delete(item)
            }
        }
}