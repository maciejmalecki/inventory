package mm.inventory.domain.itemclasses

import kotlinx.collections.immutable.ImmutableSet
import java.util.stream.Collectors

/**
 * Class of items - a template allowing creation of items.
 */
data class ItemClass(
    val name: String,
    val description: String,
    val amountUnit: UnitOfMeasurement,
    val attributes: ImmutableSet<Attribute>
) {
    private val attributesByName = attributes.stream().collect(Collectors.toMap({ it.name }, { it }))

    /**
     * Find attribute definition based on provided type name or null, if attribute with given name cannot be found.
     */
    fun findAttribute(attributeTypeName: String): Attribute? = attributesByName[attributeTypeName]

    /**
     * Gets attribute definition based on provided type name. Throws exception if attribute does not exist.
     */
    fun getAttribute(attributeTypeName: String): Attribute =
        findAttribute(attributeTypeName)
            ?: throw RuntimeException("Attribute with name '$attributeTypeName' not found.")
}

data class ItemClassVersion(
    val itemClass: ItemClass,
    val version: Int
)


