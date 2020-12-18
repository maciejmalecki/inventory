package mm.inventory.domain.items.behaviors

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.itemclass.ItemClassSelector
import mm.inventory.domain.items.ITEMS_ROLE
import mm.inventory.domain.items.ITEMS_WRITER_ROLE
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemMutator
import mm.inventory.domain.items.item.parse
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction

/**
 * Implementation of "create item" use case.
 */
class CreateItem(
    private val tx: BusinessTransaction,
    private val sec: SecurityGuard,
    private val itemClassSelector: ItemClassSelector,
    private val itemMutator: ItemMutator
) {

    /**
     * Creates new Item's instance based on provided description.
     * @param name name of the item
     * @param itemClassName name of the ItemClass
     * @param inValues attribute values specified as a "attribute name" to "string representation of attribute's value"
     */
    fun execute(name: String, itemClassName: String, inValues: ImmutableMap<String, String>): Item =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            tx.inTransaction {
                val itemClass = itemClassSelector.get(itemClassName)
                val values = itemClass.attributes.map { attribute ->
                    val rawValue = inValues[attribute.name]
                        ?: throw RuntimeException("A value for `${attribute.name}` attribute is not provided.")
                    attribute.parse(rawValue)
                }
                val item = Item(name, itemClass, values.toImmutableSet())
                itemMutator.persist(item)
                return@inTransaction item
            }
        }
}