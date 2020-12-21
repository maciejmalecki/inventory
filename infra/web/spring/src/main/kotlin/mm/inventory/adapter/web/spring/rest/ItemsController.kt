package mm.inventory.adapter.web.spring.rest

import kotlinx.collections.immutable.toImmutableMap
import mm.inventory.adapters.store.jdbi.itemclasses.createItemClassId
import mm.inventory.adapters.store.jdbi.items.createItemId
import mm.inventory.app.productplanner.item.ItemFacade
import mm.inventory.app.productplanner.item.ItemHeader
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.shared.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

data class CreateItemRequest(
    val name: String,
    val itemClassName: String,
    val inValues: List<AttributeValuation>
)

data class AttributeValuation(
    val attribute: String,
    val value: String
)

@RestController
class ItemsController(private val itemFacade: ItemFacade) {

    @GetMapping("/items")
    fun items(): ResponseEntity<List<ItemHeader>> = ResponseEntity.ok(itemFacade.findAllItems())

    @PostMapping("/items")
    fun createItem(@RequestBody requestData: CreateItemRequest): ResponseEntity<Item> =
        ResponseEntity.ok().body(
            itemFacade.createItem(
                requestData.name,
                createItemClassId(requestData.itemClassName),
                requestData.inValues.stream().collect(
                    Collectors.toMap({ v -> v.attribute }, { v -> v.value })
                ).toImmutableMap()
            )
        )

    @GetMapping("/items/{id}")
    fun item(@PathVariable id: String): ResponseEntity<Item> {
        val item = itemFacade.findById(createItemId(id))
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
    ): ResponseEntity<String> =
        try {
            itemFacade.updateItem(
                createItemId(id),
                body.stream().collect(Collectors.toMap({ it.attribute }, { it.value })).toImmutableMap()
            )
            ResponseEntity.ok().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
}
