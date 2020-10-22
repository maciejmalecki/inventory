package mm.inventory.domain.items

import mm.inventory.domain.itemclasses.ItemClass

data class Item(
        val code: String,
        val itemClass: ItemClass)