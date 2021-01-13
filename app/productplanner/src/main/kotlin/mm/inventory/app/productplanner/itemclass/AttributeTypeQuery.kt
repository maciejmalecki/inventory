package mm.inventory.app.productplanner.itemclass

import kotlinx.collections.immutable.ImmutableList

data class AttributeTypeHeader(
    val name: String,
    val scalar: Boolean)

interface AttributeTypeQuery {
    fun findAll(): ImmutableList<AttributeTypeHeader>
}