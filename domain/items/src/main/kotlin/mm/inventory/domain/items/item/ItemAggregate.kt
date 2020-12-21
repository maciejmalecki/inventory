package mm.inventory.domain.items.item

import kotlinx.collections.immutable.ImmutableSet
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.items.itemclass.DictionaryType
import mm.inventory.domain.items.itemclass.ScalarType
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.ItemId
import java.math.BigDecimal

data class Item(
    val id: ItemId,
    val name: String,
    val itemClassId: ItemClassId,
    val values: ImmutableSet<Value<*>>)

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