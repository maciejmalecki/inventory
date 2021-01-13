package mm.inventory.adapter.web.spring.rest

import mm.inventory.app.productplanner.itemclass.AttributeTypeFacade
import mm.inventory.app.productplanner.itemclass.AttributeTypeHeader
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AttributeTypesController(private val attributeTypeFacade: AttributeTypeFacade) {

    @GetMapping("/attributeTypes")
    fun attributeTypes(): ResponseEntity<List<AttributeTypeHeader>> = ResponseEntity.ok(attributeTypeFacade.findAll())
}