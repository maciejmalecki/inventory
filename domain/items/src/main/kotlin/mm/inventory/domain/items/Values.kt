package mm.inventory.domain.items

import mm.inventory.domain.itemclasses.AttributeType
import mm.inventory.domain.itemclasses.DictionaryType
import mm.inventory.domain.itemclasses.ScalarType

interface Value<in T> {
    fun type(): AttributeType<T>
    fun isValid(): Boolean
}

data class ScalarValue(val type: ScalarType, val value: Double): Value<Double> {
    override fun type() = type
    override fun isValid() = type.isValid(value)
}

data class DictionaryValue(val type: DictionaryType, val value: String): Value<String> {
    override fun type() = type
    override fun isValid() = type.isValid(value)
}