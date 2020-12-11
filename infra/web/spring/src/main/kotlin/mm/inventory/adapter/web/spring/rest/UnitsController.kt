package mm.inventory.adapter.web.spring.rest

import mm.inventory.domain.items.UnitOfMeasurement
import mm.inventory.domain.items.UnitOfMeasurementSelector
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UnitsController(private val unitOfMeasurementSelector: UnitOfMeasurementSelector) {
    @GetMapping("/units")
    fun allUnits(): List<UnitOfMeasurement> = unitOfMeasurementSelector.findAll().toList()
}