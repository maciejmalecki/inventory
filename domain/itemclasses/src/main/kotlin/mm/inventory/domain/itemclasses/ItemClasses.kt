package mm.inventory.domain.itemclasses

import kotlinx.collections.immutable.ImmutableSet

data class ItemClass(
        val name: String,
        val description: String)

data class Unit(
        val code: String,
        val name: String)

data class ScalarType(
        val unit: Unit)

data class Attribute(
        val name: String,
        val unit: Unit)

data class DictionaryValue(val value: String)

data class DictionaryType(val values: ImmutableSet<DictionaryValue>)