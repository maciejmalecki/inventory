package mm.inventory.app.productplanner.itemclass

import mm.inventory.app.productplanner.item.ManufacturerCrudRepository
import mm.inventory.domain.shared.types.ManufacturerId

class ManufacturerFacade(
    private val manufacturerCrudRepository: ManufacturerCrudRepository,
    private val manufacturerIdConverter: ManufacturerIdConverter
) {

    fun findAll() = manufacturerCrudRepository.findAll()

    fun fromManufacturerId(id: ManufacturerId) = manufacturerIdConverter.fromManufacturerId(id)

    fun toManufacturerId(id: String) = manufacturerIdConverter.toManufacturerId(id)
}