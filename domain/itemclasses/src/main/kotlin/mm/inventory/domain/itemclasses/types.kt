package mm.inventory.domain.itemclasses

import kotlinx.collections.immutable.ImmutableSet

/**
 * Unit of measurement.
 * @param code SI symbol (i.e.: s, m, kg)
 * @param name SI base unit name (i.e.: second, meter, kilogram)
 */
data class Unit(val code: String, val name: String)

interface AttributeType<T> {
    fun isValid(value: T): Boolean
}

/**
 * Scalar type holds numeric value and its unit.
 * @param unit unit of measurement for this type
 */
data class ScalarType(
        val unit: Unit) : AttributeType<Double> {
    override fun isValid(value: Double) = true
}

data class Attribute<T>(val name: String, val type: AttributeType<T>)

data class DictionaryValue(val value: String)

/**
 * Dictionary type is an enumeration of string literals.
 * @param values set of dictionary literals
 */
data class DictionaryType(val values: ImmutableSet<DictionaryValue>) : AttributeType<String> {

    override fun isValid(value: String) = values.contains(DictionaryValue(value))
}