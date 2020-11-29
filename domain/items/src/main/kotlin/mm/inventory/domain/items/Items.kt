package mm.inventory.domain.items

import kotlinx.collections.immutable.ImmutableSet
import mm.inventory.domain.itemclasses.ItemClass

data class Item(
        val name: String,
        val itemClass: ItemClass,
        val values: ImmutableSet<Value<*>>)
