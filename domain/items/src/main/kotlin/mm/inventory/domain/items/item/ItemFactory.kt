package mm.inventory.domain.items.item

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.items.manufacturer.Manufacturer
import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.transactions.BusinessTransaction
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.emptyItemId

/**
 * Implementation of "create item" use case.
 */
class ItemFactory(
    private val tx: BusinessTransaction,
    private val itemClassRepository: ItemClassRepository,
    private val itemRepository: ItemRepository
) {

    /**
     * Creates new Item's instance based on provided description.
     * @param name name of the item
     * @param itemClassId id of the ItemClass
     * @param manufacturer
     * @param manufacturersCode
     * @param inValues attribute values specified as a "attribute name" to "string representation of attribute's value"
     */
    fun create(
        name: String,
        itemClassId: ItemClassId,
        manufacturer: Manufacturer? = null,
        manufacturersCode: String? = null,
        inValues: Map<String, String>
    ): Item =
        tx.inTransaction {
            val itemClass = itemClassRepository.get(itemClassId)
            val values = itemClass.attributes.map { attribute ->
                val rawValue = inValues[attribute.name]
                    ?: throw InvalidDataException("A value for `${attribute.name}` attribute is not provided.")
                attribute.parse(rawValue)
            }
            val item = Item(
                id = emptyItemId,
                name = name,
                itemClassId = itemClassId,
                manufacturer = manufacturer,
                manufacturersCode = manufacturersCode,
                values = values.toImmutableSet(),
                categories = itemClass.proposedCategories
            )
            return@inTransaction itemRepository.persist(item)
        }
}