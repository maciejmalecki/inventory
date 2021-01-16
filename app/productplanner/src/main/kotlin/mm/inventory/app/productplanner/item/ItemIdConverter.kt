package mm.inventory.app.productplanner.item

import mm.inventory.domain.shared.types.ItemId

interface ItemIdConverter {

    fun fromItemId(id: ItemId): String

    fun toItemId(id: String): ItemId
}