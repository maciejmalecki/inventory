package mm.inventory.domain.items

import mm.inventory.domain.itemclasses.AttributeType
import mm.inventory.domain.itemclasses.DictionaryType
import mm.inventory.domain.itemclasses.ScalarType
import java.math.BigDecimal

interface Value<out T> {
    fun type(): AttributeType
    fun isValid(): Boolean
    fun value(): T
}

data class ScalarValue(val type: ScalarType, private val value: String) : Value<BigDecimal> {

    private val valid = type.isValid(value)
    private val scalarValue = if (valid) {
        BigDecimal(value)
    } else {
        BigDecimal.ZERO
    }

    override fun type() = type
    override fun isValid() = type.isValid(value)
    override fun value(): BigDecimal = scalarValue
}

data class DictionaryValue(val type: DictionaryType, private val value: String) : Value<String> {
    override fun type() = type
    override fun isValid() = type.isValid(value)
    override fun value() = value
}

fun AttributeType.parse(value: String): Value<*> =
        when(this) {
            is ScalarType -> ScalarValue(this, value)
            is DictionaryType -> DictionaryValue(this, value)
            else -> throw RuntimeException("Unknown attribute type")
        }
