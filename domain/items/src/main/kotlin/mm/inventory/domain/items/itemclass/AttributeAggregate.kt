package mm.inventory.domain.items.itemclass

import kotlinx.collections.immutable.ImmutableSet
import java.math.BigDecimal

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