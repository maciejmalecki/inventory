package mm.inventory.domain.items

import mm.inventory.domain.itemclasses.Attribute
import mm.inventory.domain.itemclasses.DictionaryType
import mm.inventory.domain.itemclasses.ScalarType
import java.math.BigDecimal

interface Value<out T> {
    fun attribute(): Attribute
    fun isValid(): Boolean
    fun value(): T
}

data class ScalarValue(val attribute: Attribute, private val value: BigDecimal, val scale: Int) : Value<BigDecimal> {
    override fun attribute() = attribute
    override fun isValid() = true
    override fun value(): BigDecimal = value
}

data class DictionaryValue(val attribute: Attribute, private val value: String) : Value<String> {
    override fun attribute() = attribute
    override fun isValid() = attribute.type.isValid(value)
    override fun value() = value
}

fun Attribute.parse(value: String): Value<*> =
    when (this.type) {
        is ScalarType -> parseScalarValue(this, value)
        is DictionaryType -> DictionaryValue(this, value)
        else -> throw RuntimeException("Unknown Attribute Type: ${this.type.javaClass.name}.")
    }

fun parseScalarValue(attribute: Attribute, value: String): ScalarValue {
    // TODO parse scale from textual representation
    val scale = 1
    val valid = attribute.type.isValid(value)
    return ScalarValue(
        attribute,
        if (valid) {
            BigDecimal(value)
        } else {
            BigDecimal.ZERO
        },
        scale
    )
}
