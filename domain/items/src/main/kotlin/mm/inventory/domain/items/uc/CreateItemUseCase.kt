package mm.inventory.domain.items.uc

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.ItemClassRepository
import mm.inventory.domain.items.ITEMS_ROLE
import mm.inventory.domain.items.ITEMS_WRITER_ROLE
import mm.inventory.domain.items.Item
import mm.inventory.domain.items.ItemRepository
import mm.inventory.domain.items.parse
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction

/**
 * Implementation of "create item" use case.
 */
class CreateItemUseCase(
    private val tx: BusinessTransaction,
    private val sec: SecurityGuard,
    private val itemClassRepository: ItemClassRepository,
    private val itemRepository: ItemRepository
) {

    /**
     * Creates new Item's instance based on provided description.
     * @param name name of the item
     * @param itemClassName name of the ItemClass
     * @param inValues attribute values specified as a "attribute name" to "string representation of attribute's value"
     */
    fun execute(name: String, itemClassName: String, inValues: ImmutableMap<String, String>): Item =
        sec.withAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            tx.inTransaction {
                val itemClass = itemClassRepository.get(itemClassName)
                val values = itemClass.attributes.map { attribute ->
                    val rawValue = inValues[attribute.name]
                        ?: throw RuntimeException("A value for `${attribute.name}` attribute is not provided.")
                    attribute.parse(rawValue)
                }
                val item = Item(name, itemClass, values.toImmutableSet())
                itemRepository.store(item)
                return@inTransaction item
            }
        }
}