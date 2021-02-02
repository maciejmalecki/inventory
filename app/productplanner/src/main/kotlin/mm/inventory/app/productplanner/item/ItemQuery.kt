package mm.inventory.app.productplanner.item

import mm.inventory.app.productplanner.itemclass.ItemClassAppId
import mm.inventory.app.productplanner.manufacturer.ManufacturerAppId
import mm.inventory.domain.shared.types.ItemId

data class ItemHeader(val id: ItemId, val name: String, val itemClassName: String)

data class ItemSearchCriteria(
    val name: String? = null,
    val manufacturerIds: List<ManufacturerAppId>? = null,
    val itemClassIds: List<ItemClassAppId>? = null,
    val manufacturersCode: String? = null
)

interface ItemQuery {
    fun findAll(): List<ItemHeader>

    fun findByCriteria(criteria: ItemSearchCriteria): List<ItemHeader>
}