package mm.inventory.domain.itemclasses

data class Attribute<in T>(val name: String, val type: AttributeType<T>)