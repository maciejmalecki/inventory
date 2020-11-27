package mm.inventory.domain.itemclasses

import kotlinx.collections.immutable.ImmutableSet

interface AttributeType<in T> {
    fun isValid(value: T): Boolean
}

/**
 * Scalar type holds numeric value and its unit.
 * @param unit unit of measurement for this type
 */
data class ScalarType(
        val unit: UnitOfMeasurement) : AttributeType<Double> {
    override fun isValid(value: Double) = true
}

data class DictionaryItem(val code: String, val value: String)

/**
 * Dictionary type is an enumeration of string literals.
 * @param items set of dictionary literals
 */
data class DictionaryType(val items: ImmutableSet<DictionaryItem>) : AttributeType<String> {

    override fun isValid(value: String) = items.map {
        it.value
    }.contains(value)
}