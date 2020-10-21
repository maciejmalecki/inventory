package mm.inventory.domain.itemclasses

data class Unit(val code: String, val name: String)

data class ItemAttribute(val name: String, val unit: Unit)