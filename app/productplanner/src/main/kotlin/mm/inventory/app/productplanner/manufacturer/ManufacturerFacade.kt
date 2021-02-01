package mm.inventory.app.productplanner.manufacturer

class ManufacturerFacade(
    private val manufacturerCrudRepository: ManufacturerCrudRepository
) {
    fun findAll() = manufacturerCrudRepository.findAll()
}