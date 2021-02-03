package mm.inventory.adapter.web.spring.rest

import mm.inventory.app.productplanner.itemclass.AttributeFacade
import mm.inventory.app.productplanner.itemclass.AttributeHeader
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AttributesController(private val attributeFacade: AttributeFacade) {

    @GetMapping("/attributes")
    fun attributeTypes(): ResponseEntity<List<AttributeHeader>> = ResponseEntity.ok(attributeFacade.findAll())
}