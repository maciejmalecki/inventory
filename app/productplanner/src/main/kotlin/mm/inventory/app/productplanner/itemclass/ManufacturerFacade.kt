package mm.inventory.app.productplanner.itemclass

import mm.inventory.app.productplanner.item.ManufacturerCrudRepository

class ManufacturerFacade(
    private val manufacturerCrudRepository: ManufacturerCrudRepository
) {
    fun findAll() = manufacturerCrudRepository.findAll()
}