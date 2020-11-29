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

data class ScalarValue(val attribute: Attribute, private val value: String) : Value<BigDecimal> {

    // TODO parse scale from textual representation
    val scale = 1
    private val valid = attribute.type.isValid(value)
    private val scalarValue = if (valid) {
        BigDecimal(value)
    } else {
        BigDecimal.ZERO
    }

    override fun attribute() = attribute
    override fun isValid() = attribute.type.isValid(value)
    override fun value(): BigDecimal = scalarValue
}

data class DictionaryValue(val attribute: Attribute, private val value: String) : Value<String> {
    override fun attribute() = attribute
    override fun isValid() = attribute.type.isValid(value)
    override fun value() = value
}

fun Attribute.parse(value: String): Value<*> =
        when(this.type) {
            is ScalarType -> ScalarValue(this, value)
            is DictionaryType -> DictionaryValue(this, value)
            else -> throw RuntimeException("Unknown attribute type")
        }
