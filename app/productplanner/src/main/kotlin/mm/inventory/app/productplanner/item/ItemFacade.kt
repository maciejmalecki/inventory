package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.app.productplanner.ITEMS_ROLE
import mm.inventory.app.productplanner.ITEMS_WRITER_ROLE
import mm.inventory.domain.items.item.ItemFactory
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemRepository
import mm.inventory.domain.items.item.Manufacturer
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
    private val itemFactory: ItemFactory,
    private val manufacturerCrudRepository: ManufacturerCrudRepository,
) {
    fun findAllItems(): ImmutableList<ItemHeader> = sec.requireRole(ITEMS_ROLE) {
        itemQuery.findAll()
    }

    fun findById(id: ItemId): Item? = sec.requireRole(ITEMS_ROLE) {
        itemRepository.findById(id)
    }

    fun createItem(
        name: String,
        itemClassId: ItemClassId,
        manufacturer: Manufacturer?,
        manufacturersCode: String?,
        inValues: Map<String, String>
    ): Item =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            // create new manufacturer when needed
            val persistentManufacturer = manufacturer?.let {
                if (manufacturer.id.empty) {
                    manufacturerCrudRepository.persist(manufacturer)
                } else {
                    manufacturer
                }
            }

            // create new item
            itemFactory.create(
                name = name,
                itemClassId = itemClassId,
                manufacturer = persistentManufacturer,
                manufacturersCode = manufacturersCode,
                inValues = inValues
            )
        }

    fun updateItem(id: ItemId, inValues: Map<String, String>) =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            tx.inTransaction {
                val item = itemRepository.get(id).mutable()
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

    fun findAllManufacturers(): ImmutableList<Manufacturer> =
        sec.requireAllRoles(ITEMS_ROLE) {
            manufacturerCrudRepository.findAll()
        }
}