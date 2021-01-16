package mm.inventory.adapters.store.jdbi.items

import mm.inventory.adapters.store.jdbi.itemclasses.asJdbiId
import mm.inventory.adapters.store.jdbi.itemclasses.createItemClassId
import mm.inventory.app.productplanner.itemclass.ItemClassIdConverter
import mm.inventory.domain.shared.types.ItemClassId

class ItemClassIdJdbiConverter : ItemClassIdConverter {
    override fun fromItemClassId(id: ItemClassId): String = id.asJdbiId().id
    override fun toItemClassId(id: String, version: Long): ItemClassId = createItemClassId(id, version)
}