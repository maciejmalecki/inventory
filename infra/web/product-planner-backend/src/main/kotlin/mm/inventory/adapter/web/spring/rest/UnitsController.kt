package mm.inventory.adapter.web.spring.rest

import mm.inventory.domain.items.itemclass.UnitOfMeasurement
import mm.inventory.domain.items.itemclass.UnitOfMeasurementRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UnitsController(private val unitOfMeasurementRepository: UnitOfMeasurementRepository) {
    @GetMapping("/units")
    fun allUnits(): List<UnitOfMeasurement> = unitOfMeasurementRepository.findAll().toList()
}