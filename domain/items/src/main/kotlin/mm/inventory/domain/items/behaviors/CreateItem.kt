package mm.inventory.domain.items.behaviors

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemMutator
import mm.inventory.domain.items.item.parse
import mm.inventory.domain.items.itemclass.ItemClassSelector
import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.transactions.BusinessTransaction
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.emptyItemId

/**
 * Implementation of "create item" use case.
 */
class CreateItem(
    private val tx: BusinessTransaction,
    private val itemClassSelector: ItemClassSelector,
    private val itemMutator: ItemMutator
) {

    /**
     * Creates new Item's instance based on provided description.
     * @param name name of the item
     * @param itemClassId id of the ItemClass
     * @param inValues attribute values specified as a "attribute name" to "string representation of attribute's value"
     */
    fun execute(name: String, itemClassId: ItemClassId, inValues: Map<String, String>): Item =
        tx.inTransaction {
            val itemClass = itemClassSelector.get(itemClassId)
            val values = itemClass.attributes.map { attribute ->
                val rawValue = inValues[attribute.name]
                    ?: throw InvalidDataException("A value for `${attribute.name}` attribute is not provided.")
                attribute.parse(rawValue)
            }
            val item = Item(emptyItemId, name, itemClassId, values.toImmutableSet())
            return@inTransaction itemMutator.persist(item)
        }
}