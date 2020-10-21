package mm.inventory.domain.itemclasses

import kotlinx.collections.immutable.ImmutableSet

data class ItemClass(
        val name: String,
        val description: String)

data class Unit(
        val code: String,
        val name: String)

interface AttributeType

data class ScalarType(
        val unit: Unit): AttributeType

data class Attribute(
        val name: String,
        val type: AttributeType)

data class DictionaryValue(val value: String)

data class DictionaryType(val values: ImmutableSet<DictionaryValue>): AttributeType