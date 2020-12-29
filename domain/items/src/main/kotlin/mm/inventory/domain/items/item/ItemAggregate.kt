package mm.inventory.domain.items.item

import io.vavr.collection.List
import io.vavr.kotlin.toVavrMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.itemclass.Attribute
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
    private val mutations: List<ItemCommand> = List.empty()
) {
    private val valuesByName: VavrMap<String, Value<*>> =
        toMap(values)

    /**
     * Execute handler over list of mutations.
     * @param handler command handler used to execute commands
     * @return item aggregate as returned by last handler
     */
    fun runMutations(handler: ItemCommandHandler): Item = runMutations(handler, mutations)

    /**
     * This method is unsafe to be used independently because it needs additional information from ItemClass aggregate,
     * therefore it is internal. Use UpdateItem domain service to update values.
     * @param inValues set of modified values
     */
    internal fun updateValues(inValues: ImmutableSet<Value<*>>): Item = copy(
        values = toMap(inValues).merge(valuesByName).values().toImmutableSet(),
        mutations = mutations.push(UpdateValuesCommand(this, inValues))
    )


    private tailrec fun runMutations(handler: ItemCommandHandler, tailMutations: List<ItemCommand>): Item =
        if (tailMutations.size() == 0) {
            this
        } else {
            handler.invoke(tailMutations.first())
            runMutations(handler, tailMutations.subSequence(1))
        }
}

private fun toMap(inValues: Set<Value<*>>): VavrMap<String, Value<*>> =
    inValues.stream().collect(Collectors.toMap({ it.attribute.name }, { it })).toVavrMap()

interface Value<out T> {
    val attribute: Attribute
    val valid: Boolean
    val value: T
}

data class ScalarValue(override val attribute: Attribute, override val value: BigDecimal, val scale: Int) :
    Value<BigDecimal> {
    override val valid = true
}

data class DictionaryValue(override val attribute: Attribute, override val value: String) : Value<String> {
    override val valid = attribute.type.isValid(value)
}
