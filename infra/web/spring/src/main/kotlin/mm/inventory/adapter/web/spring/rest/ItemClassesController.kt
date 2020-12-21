package mm.inventory.adapter.web.spring.rest

import mm.inventory.adapters.store.jdbi.itemclasses.createItemClassId
import mm.inventory.app.productplanner.itemclass.ItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassHeader
import mm.inventory.domain.items.itemclass.ItemClass
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ItemClassesController(private val itemClassFacade: ItemClassFacade) {

    @GetMapping("/itemClasses")
    fun itemClasses(): ResponseEntity<List<ItemClassHeader>> = ResponseEntity.ok(itemClassFacade.findAll())

    @GetMapping("/itemClasses/{id}")
    fun itemClass(@PathVariable id: String): ResponseEntity<ItemClass> {
        val itemClass = itemClassFacade.findById(createItemClassId(id))
        return if (itemClass != null) {
            ResponseEntity.ok(itemClass)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}