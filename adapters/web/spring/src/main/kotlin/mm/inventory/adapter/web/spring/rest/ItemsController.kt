package mm.inventory.adapter.web.spring.rest

import kotlinx.collections.immutable.toImmutableMap
import mm.inventory.domain.items.Item
import mm.inventory.domain.items.ItemCreator
import org.jdbi.v3.core.Jdbi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/items")
class ItemsController(private val db: Jdbi, private val itemCreator: ItemCreator) {

    @PostMapping
    fun createItem(@RequestBody requestData: CreateItemRequest): ResponseEntity<Item> =
            ResponseEntity.ok().body(db.inTransaction<Item, RuntimeException> {
                itemCreator.create(requestData.name, requestData.itemClassName, requestData.inValues.toImmutableMap())
            })
}

data class CreateItemRequest(val name: String, val itemClassName: String, val inValues: Map<String, String>)