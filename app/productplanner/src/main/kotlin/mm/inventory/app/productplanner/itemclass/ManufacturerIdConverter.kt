package mm.inventory.app.productplanner.itemclass

import mm.inventory.domain.shared.types.ManufacturerId

interface ManufacturerIdConverter {

    fun fromManufacturerId(id: ManufacturerId): String

    fun toManufacturerId(id: String): ManufacturerId
}