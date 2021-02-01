package mm.inventory.adapter.web.spring.rest

import mm.inventory.app.productplanner.manufacturer.ManufacturerFacade
import mm.inventory.app.productplanner.manufacturer.asAppId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class ManufacturerProjection(val id: Long?, val name: String)


@RestController
class ManufacturerController(private val manufacturerFacade: ManufacturerFacade) {

    @GetMapping("/manufacturers")
    fun manufacturers(): ResponseEntity<List<ManufacturerProjection>> =
        ResponseEntity.ok(manufacturerFacade.findAll().map { ManufacturerProjection(it.id.asAppId().id, it.name) })
}