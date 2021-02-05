package mm.inventory.domain.items.item

import io.vavr.kotlin.toVavrMap
import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.items.manufacturer.Manufacturer
import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.mutations.Mutable
import mm.inventory.domain.shared.mutations.MutatingCommand
import mm.inventory.domain.shared.types.CategoryId
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
 * @param manufacturer item's manufacturer link if specified
 * @param manufacturersCode serial code of the item when specified by manufacturer
 * @param values of the aggregate
 * @param categories to which given item belongs
 */
data class Item(
    val id: ItemId,
    val name: String,
    val itemClassId: ItemClassId,
    val manufacturer: Manufacturer? = null,
    val manufacturersCode: String? = null,
    val values: Set<Value>,
    val categories: Set<CategoryId>
) {
    /**
     * Values indexed by name for sake of convenience.
     */
    val valuesByName: VavrMap<String, Value> = toMap(values)

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

    /**
     * Update manufacturer command.
     * @param manufacturer to be set
     * @param manufacturersCode to be set
     */
    fun updateManufacturer(manufacturer: Manufacturer, manufacturersCode: String?): MutableItem {
        append(
            UpdateManufacturerCommand(snapshot, manufacturer, manufacturersCode),
            snapshot.copy(manufacturer = manufacturer, manufacturersCode = manufacturersCode)
        )
        return this
    }

    /**
     * Remove manufacturer assignment.
     */
    fun removeManufacturer(): MutableItem {
        append(
            RemoveManufacturerCommand(snapshot),
            snapshot.copy(manufacturer = null)
        )
        return this
    }

    fun addCategory(category: CategoryId): MutableItem {
        if (snapshot.categories.contains(category)) {
            throw InvalidDataException("Category $category is already assigned to the ${snapshot.id}.")
        }
        append(
            AddCategoryCommand(snapshot, category),
            snapshot.copy(categories = (snapshot.categories + category).toImmutableSet())
        )
        return this
    }

    fun removeCategory(category: CategoryId): MutableItem {
        if (!snapshot.categories.contains(category)) {
            throw InvalidDataException("Category $category is not assigned to the ${snapshot.id}.")
        }
        append(
            RemoveCategoryCommand(snapshot, category),
            snapshot.copy(categories = (snapshot.categories - category).toImmutableSet())
        )
        return this
    }
}

interface Value {
    val attribute: Attribute
    val valid: Boolean
}

data class ScalarValue(override val attribute: Attribute, val value: BigDecimal, val scale: Int) :
    Value {
    override val valid = true
}

data class DictionaryValue(override val attribute: Attribute, val value: String) : Value {
    override val valid = attribute.type.isValid(value)
}

data class UpdateValuesCommand(
    override val base: Item,
    val values: Set<Value>
) : MutatingCommand<Item>

data class UpdateManufacturerCommand(
    override val base: Item,
    val manufacturer: Manufacturer,
    val manufacturersCode: String?
) : MutatingCommand<Item>

data class RemoveManufacturerCommand(
    override val base: Item
) : MutatingCommand<Item>

data class AddCategoryCommand(
    override val base: Item,
    val category: CategoryId
) : MutatingCommand<Item>

data class RemoveCategoryCommand(
    override val base: Item,
    val category: CategoryId
) : MutatingCommand<Item>

private fun toMap(values: Set<Value>): VavrMap<String, Value> =
    values.stream().collect(Collectors.toMap({ it.attribute.name }, { it })).toVavrMap()
