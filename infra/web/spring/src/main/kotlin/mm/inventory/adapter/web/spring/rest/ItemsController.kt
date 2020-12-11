package mm.inventory.adapter.web.spring.rest

import kotlinx.collections.immutable.toImmutableMap
import mm.inventory.app.itemsfacade.item.ItemFacade
import mm.inventory.app.itemsfacade.item.ItemHeader
import mm.inventory.domain.items.Item
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
                requestData.itemClassName,
                requestData.inValues.stream().collect(
                    Collectors.toMap({ v -> v.attribute }, { v -> v.value })
                ).toImmutableMap()
            )
        )

    @GetMapping("/items/{itemName}")
    fun item(@PathVariable itemName: String): ResponseEntity<Item> {
        val item = itemFacade.findByName(itemName)
        return if (item != null) {
            ResponseEntity.ok(item)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
