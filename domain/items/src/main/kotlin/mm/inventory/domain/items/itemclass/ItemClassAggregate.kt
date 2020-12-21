package mm.inventory.domain.items.itemclass

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableMap
import mm.inventory.domain.shared.NotFoundException
import mm.inventory.domain.shared.types.ItemClassId
import java.math.BigDecimal
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

    /**
     * Gets attribute definition based on provided type name. Throws exception if attribute does not exist.
     */
    fun getAttribute(attributeTypeName: String): Attribute =
        attributesByName[attributeTypeName]
            ?: throw NotFoundException("Attribute with name '$attributeTypeName' not found.")
}

data class ItemClassVersion(
    val itemClass: ItemClass,
    val version: Int
)


data class Attribute(val name: String, val type: AttributeType)
interface AttributeType {
    fun isValid(value: String): Boolean
}

/**
 * Scalar type holds numeric value and its unit.
 * @param unit unit of measurement for this type
 */
data class ScalarType(
    val unit: UnitOfMeasurement
) : AttributeType {

    override fun isValid(value: String): Boolean =
        try {
            BigDecimal(value)
            true
        } catch (e: NumberFormatException) {
            false
        }

}

data class DictionaryItem(val code: String, val value: String)

/**
 * Dictionary type is an enumeration of string literals.
 * @param items set of dictionary literals
 */
data class DictionaryType(val items: ImmutableSet<DictionaryItem>) : AttributeType {

    override fun isValid(value: String) = items.map {
        it.code
    }.contains(value)

}