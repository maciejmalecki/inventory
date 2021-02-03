package mm.inventory.adapter.web.spring.rest

import mm.inventory.app.productplanner.itemclass.DraftItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassAppId
import mm.inventory.app.productplanner.itemclass.ItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassHeader
import mm.inventory.app.productplanner.itemclass.asAppId
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.items.itemclass.DictionaryItem
import mm.inventory.domain.items.itemclass.DictionaryType
import mm.inventory.domain.items.itemclass.ItemClass
import mm.inventory.domain.items.itemclass.ScalarType
import mm.inventory.domain.items.itemclass.UnitOfMeasurement
import mm.inventory.domain.shared.InvalidDataException
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

data class ItemClassProjection(
    val id: ItemClassIdProjection,
    val name: String,
    val description: String,
    val amountUnit: UnitOfMeasurement,
    val attributes: List<AttributeProjection>
)

internal fun ItemClass.toProjection(): ItemClassProjection = ItemClassProjection(
    id = ItemClassIdProjection(id.asAppId().id, id.asAppId().version),
    name = name,
    description = description,
    amountUnit = amountUnit,
    attributes = attributes.map { it.toProjection() }
)

interface AttributeProjection {
    val name: String
}

data class ScalarAttributeProjection(
    override val name: String,
    val unit: UnitOfMeasurement
) : AttributeProjection

data class DictionaryAttributeProjection(
    override val name: String,
    val items: Set<DictionaryItem>
) : AttributeProjection

internal fun Attribute.toProjection(): AttributeProjection = when (type) {
    is ScalarType -> ScalarAttributeProjection(
        name = name,
        unit = (type as ScalarType).unit
    )
    is DictionaryType -> DictionaryAttributeProjection(
        name = name,
        items = (type as DictionaryType).items
    )
    else -> throw InvalidDataException("Unrecognized attribute type: ${type.javaClass.name}.")
}

@RestController
class ItemClassesController(
    private val itemClassFacade: ItemClassFacade,
    private val draftItemClassFacade: DraftItemClassFacade
) {

    @GetMapping("/itemClasses")
    fun itemClasses(): ResponseEntity<List<ItemClassHeader>> = ResponseEntity.ok(itemClassFacade.findAll())

    @GetMapping("/itemClasses/{id}")
    fun itemClass(@PathVariable id: String): ResponseEntity<ItemClassProjection> {
        val itemClass = itemClassFacade.findById(ItemClassAppId(id))
        return if (itemClass != null) {
            ResponseEntity.ok(itemClass.toProjection())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/itemClasses/{id}/draft")
    fun draftItemClass(@PathVariable id: String): ResponseEntity<ItemClassProjection> {
        val draftItemClass = draftItemClassFacade.findDraftById(ItemClassAppId(id))
        return if (draftItemClass != null) {
            ResponseEntity.ok(draftItemClass.itemClass.toProjection())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/itemClasses/{id}/draft")
    fun newDraftItemClass(@PathVariable id: String): ResponseEntity<ItemClassProjection> {
        val draftItemClass = draftItemClassFacade.createDraft(ItemClassAppId(id))
        return ResponseEntity.ok(draftItemClass.itemClass.toProjection())
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
    fun completeDraftItemClass(@PathVariable id: String): ResponseEntity<ItemClassProjection> =
        ResponseEntity.ok(draftItemClassFacade.completeDraft(ItemClassAppId(id)).toProjection())

    @DeleteMapping("/itemClasses/{id}/draft")
    fun rejectDraftItemClass(@PathVariable id: String): ResponseEntity<Any> {
        draftItemClassFacade.rejectDraft(ItemClassAppId(id))
        return ResponseEntity.ok().build()
    }
}