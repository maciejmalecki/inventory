package mm.inventory.adapter.web.spring.rest

import mm.inventory.app.productplanner.item.ItemFacade
import mm.inventory.app.productplanner.item.ItemHeader
import mm.inventory.app.productplanner.itemclass.ItemClassFacade
import mm.inventory.app.productplanner.itemclass.ManufacturerFacade
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.Manufacturer
import mm.inventory.domain.shared.types.emptyManufacturerId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

data class CreateItemRequest(
    val name: String,
    val itemClassName: String,
    val itemClassVersion: Long,
    val manufacturer: ManufacturerData?,
    val manufacturersCode: String?,
    val inValues: List<AttributeValuation>
)

data class ManufacturerData(val id: String?, val name: String)

data class AttributeValuation(
    val attribute: String,
    val value: String
)

@RestController
class ItemsController(
    private val itemFacade: ItemFacade,
    private val itemClassFacade: ItemClassFacade,
    private val manufacturerFacade: ManufacturerFacade) {

    @GetMapping("/items")
    fun items(): ResponseEntity<List<ItemHeader>> = ResponseEntity.ok(itemFacade.findAllItems())

    @PostMapping("/items")
    fun createItem(@RequestBody requestData: CreateItemRequest): ResponseEntity<Item> =
        ResponseEntity.ok().body(
            itemFacade.createItem(
                name = requestData.name,
                itemClassId = itemClassFacade.toItemClassId(requestData.itemClassName, requestData.itemClassVersion),
                manufacturer = requestData.manufacturer?.let { toManufacturer(requestData.manufacturer) },
                manufacturersCode = requestData.manufacturersCode,
                inValues = requestData.inValues.stream().collect(
                    Collectors.toMap({ v -> v.attribute }, { v -> v.value })
                )
            )
        )

    @GetMapping("/items/{id}")
    fun item(@PathVariable id: String): ResponseEntity<Item> {
        val item = itemFacade.findById(itemFacade.toItemId(id))
        return if (item != null) {
            ResponseEntity.ok(item)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/items/{id}")
    fun updateItem(
        @PathVariable id: String,
        @RequestBody body: List<AttributeValuation>
    ): ResponseEntity<Any> =
        ResponseEntity.ok(
            itemFacade.updateItem(
                itemFacade.toItemId(id),
                body.stream().collect(Collectors.toMap({ it.attribute }, { it.value }))
            )
        )

    @DeleteMapping("/items/{id}")
    fun deleteItem(@PathVariable id: String): ResponseEntity<Any> {
        itemFacade.deleteItem(itemFacade.toItemId(id))
        return ResponseEntity.ok().build()
    }

    private fun toManufacturer(data: ManufacturerData) = Manufacturer(
        id = data.id?.let { manufacturerFacade.toManufacturerId(data.id) } ?: emptyManufacturerId,
        name = data.name
    )
}
