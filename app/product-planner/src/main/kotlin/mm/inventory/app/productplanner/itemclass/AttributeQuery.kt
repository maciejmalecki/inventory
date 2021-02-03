package mm.inventory.app.productplanner.itemclass

import kotlinx.collections.immutable.ImmutableList

data class AttributeHeader(
    val name: String,
    val scalar: Boolean,
    val unitCode: String?,
    val unitName: String?
)

interface AttributeQuery {
    fun findAll(): ImmutableList<AttributeHeader>
}