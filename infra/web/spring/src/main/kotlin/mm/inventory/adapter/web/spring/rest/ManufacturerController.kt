package mm.inventory.adapter.web.spring.rest

import mm.inventory.app.productplanner.itemclass.ManufacturerFacade
import mm.inventory.domain.items.item.Manufacturer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ManufacturerController(private val manufacturerFacade: ManufacturerFacade) {

    @GetMapping("/manufacturers")
    fun manufacturers(): ResponseEntity<List<Manufacturer>> = ResponseEntity.ok(manufacturerFacade.findAll())
}