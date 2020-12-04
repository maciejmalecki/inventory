package mm.inventory.domain.items

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.itemclasses.ItemClassRepository

/**
 * Implementation of "create item" use case.
 */
class ItemCreator(private val itemClassRepository: ItemClassRepository, private val itemRepository: ItemRepository) {

    /**
     * Creates new Item's instance based on provided description.
     * @param name name of the item
     * @param itemClassName name of the ItemClass
     * @param inValues attribute values specified as a "attribute name" to "string representation of attribute's value"
     */
    fun create(name: String, itemClassName: String, inValues: ImmutableMap<String, String>): Item {
        val itemClass = itemClassRepository.findByName(itemClassName)
                ?: throw RuntimeException("Item class `$itemClassName` not found.")

        val values = itemClass.attributes.map { attribute ->
            val rawValue = inValues[attribute.name]
                    ?: throw RuntimeException("A value for `${attribute.name}` attribute is not provided.")
            attribute.parse(rawValue)
        }.toImmutableSet()

        val item = Item(name, itemClass, values)
        itemRepository.store(item)
        return item
    }
}