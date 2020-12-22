package mm.inventory.domain.items.item

import kotlinx.collections.immutable.ImmutableSet
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.items.itemclass.DictionaryType
import mm.inventory.domain.items.itemclass.ScalarType
import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.ItemId
import java.math.BigDecimal

data class Item(
    val id: ItemId,
    val name: String,
    val itemClassId: ItemClassId,
    val values: ImmutableSet<Value<*>>
)

interface Value<out T> {
    fun attribute(): Attribute
    fun isValid(): Boolean
    fun getValue(): T
}

data class ScalarValue(val attribute: Attribute, private val value: BigDecimal, val scale: Int) : Value<BigDecimal> {
    override fun attribute() = attribute
    override fun isValid() = true
    override fun getValue(): BigDecimal = value
}

data class DictionaryValue(val attribute: Attribute, private val value: String) : Value<String> {
    override fun attribute() = attribute
    override fun isValid() = attribute.type.isValid(value)
    override fun getValue() = value
}

/**
 * Parses given string representation into the Value according to given Attribute.
 * @param value textual representation
 * @return parsed value
 * @throws InvalidDataException if attribute type is not supported or if format of data is incorrect.
 */
fun Attribute.parse(value: String): Value<*> =
    when (type) {
        is ScalarType -> parseScalarValue(value)
        is DictionaryType -> parseDictionaryValue(value)
        else -> throw InvalidDataException("Unknown Attribute Type: ${this.type.javaClass.name}.")
    }

private fun Attribute.parseDictionaryValue(value: String): DictionaryValue =
    if (type.isValid(value)) {
        DictionaryValue(this, value)
    } else {
        throw InvalidDataException("Illegal value $value for dictionary type $name.")
    }

private fun Attribute.parseScalarValue(value: String): ScalarValue {
    // TODO parse scale from textual representation
    val scale = 1
    val valid = type.isValid(value)
    return ScalarValue(
        this,
        if (valid) {
            BigDecimal(value)
        } else {
            BigDecimal.ZERO
        },
        scale
    )
}