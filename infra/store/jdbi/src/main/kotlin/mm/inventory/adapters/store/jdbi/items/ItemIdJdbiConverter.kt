package mm.inventory.adapters.store.jdbi.items

import mm.inventory.app.productplanner.item.ItemIdConverter
import mm.inventory.domain.shared.types.ItemId

class ItemIdJdbiConverter : ItemIdConverter {
    override fun fromItemId(id: ItemId): String = id.asJdbiId().id

    override fun toItemId(id: String): ItemId = createItemId(id)
}