package mm.inventory.adapter.web.spring.rest

import mm.inventory.adapters.store.jdbi.itemclasses.createItemClassId
import mm.inventory.app.productplanner.itemclass.ItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassHeader
import mm.inventory.domain.items.itemclass.ItemClass
import mm.inventory.domain.items.itemclass.UnitOfMeasurement
import mm.inventory.domain.shared.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class UpdateDraftRequest(val description: String?, val unitCode: UnitOfMeasurement?)

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

    @GetMapping("/itemClasses/{id}/draft")
    fun draftItemClass(@PathVariable id: String): ResponseEntity<ItemClass> =
        try {
            val draftItemClass = itemClassFacade.findDraftById(createItemClassId(id))
            if (draftItemClass != null) {
                ResponseEntity.ok(draftItemClass.itemClass)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }

    @PutMapping("/itemClasses/{id}/draft")
    fun newDraftItemClass(@PathVariable id: String): ResponseEntity<ItemClass> =
        try {
            val draftItemClass = itemClassFacade.createDraft(createItemClassId(id))
            ResponseEntity.ok(draftItemClass.itemClass)
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }

    @PostMapping("/itemClasses/{id}/draft")
    fun updateDraftItemClass(@PathVariable id: String, @RequestBody body: UpdateDraftRequest): ResponseEntity<Any> =
        try {
            val draftItemClass = itemClassFacade.findDraftById(createItemClassId(id))
                ?: throw NotFoundException("Draft item class for $id.")
            if (body.description != null) {
                draftItemClass.changeDescription(body.description)
            }
            if (body.unitCode != null) {
                draftItemClass.changeAmountUnit(body.unitCode)
            }
            if (draftItemClass.hasMutations) {
                itemClassFacade.save(draftItemClass)
            }
            ResponseEntity.ok().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }

    @PostMapping("/itemClasses/{id}/draft/complete")
    fun completeDraftItemClass(@PathVariable id: String): ResponseEntity<ItemClass> =
        try {
            val draftItemClass = itemClassFacade.findDraftById(createItemClassId(id))
            if (draftItemClass != null) {
                ResponseEntity.ok(itemClassFacade.completeDraft(draftItemClass))
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }

    @DeleteMapping("/itemClasses/{id}/draft")
    fun rejectDraftItemClass(@PathVariable id: String): ResponseEntity<Any> =
        try {
            val draftItemClass = itemClassFacade.findDraftById(createItemClassId(id))
            if (draftItemClass != null) {
                itemClassFacade.rejectDraft(draftItemClass)
                ResponseEntity.ok("rejected")
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
}