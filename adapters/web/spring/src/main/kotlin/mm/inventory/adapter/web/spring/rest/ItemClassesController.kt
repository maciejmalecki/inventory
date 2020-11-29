package mm.inventory.adapter.web.spring.rest

import mm.inventory.domain.itemclasses.ItemClass
import mm.inventory.domain.itemclasses.ItemClassRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ItemClassesController(private val itemClassRepository: ItemClassRepository) {

    @GetMapping("/itemClasses/{itemClassName}")
    fun itemClass(@PathVariable itemClassName: String): ResponseEntity<ItemClass> {
        val itemClass = itemClassRepository.findByName(itemClassName)
        return if (itemClass != null) {
            ResponseEntity.ok(itemClass)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}