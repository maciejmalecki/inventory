package mm.inventory.adapter.web.spring.rest

import kotlinx.collections.immutable.toImmutableMap
import mm.inventory.domain.items.Item
import mm.inventory.domain.items.ItemCreator
import org.jdbi.v3.core.Jdbi
import org.springframework.http.ResponseEntity
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
class ItemsController(private val db: Jdbi, private val itemCreator: ItemCreator) {

    @PostMapping("/items")
    fun createItem(@RequestBody requestData: CreateItemRequest): ResponseEntity<Item> =
        ResponseEntity.ok().body(db.inTransaction<Item, RuntimeException> {
            itemCreator.create(
                requestData.name,
                requestData.itemClassName,
                requestData.inValues.stream().collect(
                    Collectors.toMap({ v -> v.attribute }, { v -> v.value })
                ).toImmutableMap()
            )
        })
}
