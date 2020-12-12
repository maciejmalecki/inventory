package mm.inventory.adapter.web.spring.rest

import mm.inventory.app.itemsfacade.itemclass.ItemClassFacade
import mm.inventory.app.itemsfacade.itemclass.ItemClassHeader
import mm.inventory.domain.items.ItemClass
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ItemClassesController(private val itemClassFacade: ItemClassFacade) {

    @GetMapping("/itemClasses")
    fun itemClasses(): ResponseEntity<List<ItemClassHeader>> = ResponseEntity.ok(itemClassFacade.findAll())

    @GetMapping("/itemClasses/{itemClassName}")
    fun itemClass(@PathVariable itemClassName: String): ResponseEntity<ItemClass> {
        val itemClass = itemClassFacade.findByName(itemClassName)
        return if (itemClass != null) {
            ResponseEntity.ok(itemClass)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}