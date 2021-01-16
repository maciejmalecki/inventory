package mm.inventory.domain.items.item

import io.vavr.kotlin.toVavrMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.mutations.Mutable
import mm.inventory.domain.shared.mutations.MutatingCommand
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.ItemId
import java.math.BigDecimal
import java.util.stream.Collectors
import io.vavr.collection.Map as VavrMap

/**
 * Item aggregate.
 *
 * @param id of the aggregate
 * @param name of the aggregate
 * @param itemClassId link to the ItemClass aggregate
 * @param values of the aggregate
 * @param mutations list of aggregate mutations
 */
data class Item(
    val id: ItemId,
    val name: String,
    val itemClassId: ItemClassId,
    val manufacturer: Manufacturer? = null,
    val manufacturersCode: String? = null,
    val values: ImmutableSet<Value<*>>
) {
    /**
     * Values indexed by name for sake of convenience.
     */
    val valuesByName: VavrMap<String, Value<*>> = toMap(values)

    fun mutable() = MutableItem(this)
}

class MutableItem(_snapshot: Item) : Mutable<Item>(_snapshot) {
    val item: Item
        get() = snapshot

    /**
     * Update values command
     * @param inValues set of modified values
     */
    fun updateValues(inValues: Map<String, String>): MutableItem {
        val values = inValues.entries.map {
            val value = snapshot.valuesByName[it.key].orNull
                ?: throw InvalidDataException("Attribute ${it.key} does not exist in item ${snapshot.name}.")
            value.attribute.parse(it.value)
        }.toImmutableSet()

        append(
            UpdateValuesCommand(snapshot, values),
            snapshot.copy(
                values = toMap(values).merge(snapshot.valuesByName).values().toImmutableSet()
            )
        )
        return this
    }
}

interface Value<out T> {
    val attribute: Attribute
    val valid: Boolean
    val data: T
}

data class ScalarValue(override val attribute: Attribute, override val data: BigDecimal, val scale: Int) :
    Value<BigDecimal> {
    override val valid = true
}

data class DictionaryValue(override val attribute: Attribute, override val data: String) : Value<String> {
    override val valid = attribute.type.isValid(data)
}

data class UpdateValuesCommand(
    override val base: Item,
    val values: ImmutableSet<Value<*>>
) : MutatingCommand<Item>

private fun toMap(values: Set<Value<*>>): VavrMap<String, Value<*>> =
    values.stream().collect(Collectors.toMap({ it.attribute.name }, { it })).toVavrMap()
