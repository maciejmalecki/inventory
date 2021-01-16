package mm.inventory.app.productplanner.itemclass

import mm.inventory.domain.shared.types.ItemClassId

interface ItemClassIdConverter {

    fun fromItemClassId(id: ItemClassId): String

    fun toItemClassId(id: String, version: Long = -1L): ItemClassId
}