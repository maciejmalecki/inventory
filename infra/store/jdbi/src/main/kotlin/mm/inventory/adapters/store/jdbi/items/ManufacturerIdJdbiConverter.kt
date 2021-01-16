package mm.inventory.adapters.store.jdbi.items

import mm.inventory.app.productplanner.itemclass.ManufacturerIdConverter
import mm.inventory.domain.shared.types.ManufacturerId

class ManufacturerIdJdbiConverter : ManufacturerIdConverter {

    override fun fromManufacturerId(id: ManufacturerId): String = id.asJdbiId().id.toString()

    override fun toManufacturerId(id: String): ManufacturerId = createManufacturerId(id.toLong())
}