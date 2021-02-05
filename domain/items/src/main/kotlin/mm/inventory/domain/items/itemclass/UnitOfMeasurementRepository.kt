package mm.inventory.domain.items.itemclass

import mm.inventory.domain.shared.NotFoundException

interface UnitOfMeasurementRepository {
    fun findAll():List<UnitOfMeasurement>
    fun findByCode(code: String): UnitOfMeasurement?
    fun get(code: String): UnitOfMeasurement = findByCode(code) ?: throw NotFoundException("Unit $code not found.")
}