package mm.inventory.adapter.web.spring.rest

import mm.inventory.app.productplanner.itemclass.DraftItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassAppId
import mm.inventory.app.productplanner.itemclass.ItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassHeader
import mm.inventory.domain.items.itemclass.ItemClass
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class UpdateDraftRequest(
    val description: String?,
    val unitCode: String?,
    val addedAttributes: List<String>,
    val removedAttributes: List<String>
)

@RestController
class ItemClassesController(
    private val itemClassFacade: ItemClassFacade,
    private val draftItemClassFacade: DraftItemClassFacade
) {

    @GetMapping("/itemClasses")
    fun itemClasses(): ResponseEntity<List<ItemClassHeader>> = ResponseEntity.ok(itemClassFacade.findAll())

    @GetMapping("/itemClasses/{id}")
    fun itemClass(@PathVariable id: String): ResponseEntity<ItemClass> {
        val itemClass = itemClassFacade.findById(ItemClassAppId(id))
        return if (itemClass != null) {
            ResponseEntity.ok(itemClass)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/itemClasses/{id}/draft")
    fun draftItemClass(@PathVariable id: String): ResponseEntity<ItemClass> {
        val draftItemClass = draftItemClassFacade.findDraftById(ItemClassAppId(id))
        return if (draftItemClass != null) {
            ResponseEntity.ok(draftItemClass.itemClass)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/itemClasses/{id}/draft")
    fun newDraftItemClass(@PathVariable id: String): ResponseEntity<ItemClass> {
        val draftItemClass = draftItemClassFacade.createDraft(ItemClassAppId(id))
        return ResponseEntity.ok(draftItemClass.itemClass)
    }

    @PostMapping("/itemClasses/{id}/draft")
    fun updateDraftItemClass(@PathVariable id: String, @RequestBody body: UpdateDraftRequest): ResponseEntity<Any> {
        draftItemClassFacade.updateDraft(
            id = ItemClassAppId(id),
            description = body.description,
            unitCode = body.unitCode,
            addedAttributeTypes = body.addedAttributes,
            removedAttributeTypes = body.removedAttributes
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/itemClasses/{id}/draft/complete")
    fun completeDraftItemClass(@PathVariable id: String): ResponseEntity<ItemClass> =
        ResponseEntity.ok(draftItemClassFacade.completeDraft(ItemClassAppId(id)))

    @DeleteMapping("/itemClasses/{id}/draft")
    fun rejectDraftItemClass(@PathVariable id: String): ResponseEntity<Any> {
        draftItemClassFacade.rejectDraft(ItemClassAppId(id))
        return ResponseEntity.ok().build()
    }
}