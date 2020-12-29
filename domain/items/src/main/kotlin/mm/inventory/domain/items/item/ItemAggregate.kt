package mm.inventory.domain.items.item

import io.vavr.kotlin.toVavrMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.shared.changetracking.MutatingCommand
import mm.inventory.domain.shared.changetracking.MutatingCommandHandler
import mm.inventory.domain.shared.changetracking.Mutations
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.ItemId
import java.math.BigDecimal
import java.util.stream.Collectors
import io.vavr.collection.Map as VavrMap


data class Item(
    val id: ItemId,
    val name: String,
    val itemClassId: ItemClassId,
    val values: ImmutableSet<Value<*>>,
    internal val mutations: Mutations<Item> = Mutations()
) {
    /**
     * Values indexed by name for sake of convenience.
     */
    internal val valuesByName: VavrMap<String, Value<*>> =
        toMap(values)

    /**
     * Execute handler over list of mutations.
     * @param handler command handler used to execute commands
     * @return item aggregate as returned by last handler
     */
    fun handleAll(handler: MutatingCommandHandler<Item>): Item = mutations.handleAll(handler)

    /**
     * This method is unsafe to be used independently because it needs additional information from ItemClass aggregate,
     * therefore it is internal. Use UpdateItem domain service to update values.
     * @param inValues set of modified values
     */
    internal fun updateValues(inValues: ImmutableSet<Value<*>>): Item =
        UpdateValuesCommand(this, inValues).mutate()
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

private fun toMap(inValues: Set<Value<*>>): VavrMap<String, Value<*>> =
    inValues.stream().collect(Collectors.toMap({ it.attribute.name }, { it })).toVavrMap()

data class UpdateValuesCommand(
    override val base: Item,
    val values: ImmutableSet<Value<*>>
) : MutatingCommand<Item> {

    override fun mutate(): Item = base.copy(
        values = toMap(values).merge(base.valuesByName).values().toImmutableSet(),
        mutations = base.mutations.append(this)
    )
}