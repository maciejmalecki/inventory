package mm.inventory.domain.items.itemclass

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableMap
import mm.inventory.domain.shared.NotFoundException
import mm.inventory.domain.shared.types.ItemClassId
import java.util.stream.Collectors

/**
 * Class of items - a template allowing creation of items.
 */
data class ItemClass(
    val id: ItemClassId,
    val name: String,
    val description: String,
    val amountUnit: UnitOfMeasurement,
    val attributes: ImmutableSet<Attribute>
) {
    private val attributesByName = attributes.stream().collect(Collectors.toMap({ it.name }, { it })).toImmutableMap()

    fun hasAttribute(name: String) = attributesByName.contains(name)

    /**
     * Gets attribute definition based on provided type name. Throws exception if attribute does not exist.
     */
    fun getAttribute(attributeTypeName: String): Attribute =
        attributesByName[attributeTypeName]
            ?: throw NotFoundException("Attribute with name '$attributeTypeName' not found.")
}
