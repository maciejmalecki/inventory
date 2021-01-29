package mm.inventory.domain.items.item

import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.items.itemclass.DictionaryType
import mm.inventory.domain.items.itemclass.ScalarType
import mm.inventory.domain.shared.InvalidDataException
import java.math.BigDecimal

/**
 * Parses given string representation into the Value according to given Attribute.
 * @param value textual representation
 * @return parsed value
 * @throws InvalidDataException if attribute type is not supported or if format of data is incorrect.
 */
fun Attribute.parse(value: String): Value =
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
