package mm.inventory.adapter.web.spring.rest

import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.app.productplanner.item.ItemFacade
import mm.inventory.app.productplanner.item.ItemHeader
import mm.inventory.app.productplanner.item.asAppId
import mm.inventory.app.productplanner.manufacturer.asAppId
import mm.inventory.app.productplanner.itemclass.ItemClassAppId
import mm.inventory.app.productplanner.itemclass.asAppId
import mm.inventory.app.productplanner.manufacturer.ManufacturerAppId
import mm.inventory.domain.items.item.DictionaryValue
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.manufacturer.Manufacturer
import mm.inventory.domain.items.item.ScalarValue
import mm.inventory.domain.items.itemclass.DictionaryItem
import mm.inventory.domain.items.itemclass.DictionaryType
import mm.inventory.domain.items.itemclass.ScalarType
import mm.inventory.domain.items.itemclass.UnitOfMeasurement
import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.emptyManufacturerId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.stream.Collectors

data class CreateItemRequest(
    val name: String,
    val itemClassName: String,
    val itemClassVersion: Long,
    val manufacturer: ManufacturerProjection?,
    val manufacturersCode: String?,
    val inValues: List<AttributeValuation>
)

data class UpdateItemRequest(
    val manufacturer: ManufacturerProjection?,
    val inValues: List<AttributeValuation>
)

data class ItemProjection(
    val id: String,
    val name: String,
    val itemClassId: ItemClassIdProjection,
    val manufacturer: ManufacturerProjection?,
    val manufacturersCode: String?,
    val values: List<ValueProjection>
)

data class ItemClassIdProjection(
    val id: String,
    val version: Long
)

interface ValueProjection {
    val name: String
}

data class ScalarProjection(
    override val name: String,
    val value: BigDecimal,
    val unit: UnitOfMeasurement
) : ValueProjection

data class DictionaryProjection(
    override val name: String,
    val value: String,
    val items: Set<DictionaryItem>
) : ValueProjection

data class AttributeValuation(
    val attribute: String,
    val value: String
)

@RestController
class ItemsController(
    private val itemFacade: ItemFacade
) {

    @GetMapping("/items")
    fun items(): ResponseEntity<List<ItemHeader>> = ResponseEntity.ok(itemFacade.findAllItems())

    @PostMapping("/items")
    fun createItem(@RequestBody requestData: CreateItemRequest): ResponseEntity<ItemProjection> =
        ResponseEntity.ok().body(
            toProjection(
                itemFacade.createItem(
                    name = requestData.name,
                    itemClassId = ItemClassAppId(requestData.itemClassName, requestData.itemClassVersion),
                    manufacturer = requestData.manufacturer?.let { toManufacturer(requestData.manufacturer) },
                    manufacturersCode = requestData.manufacturersCode,
                    inValues = requestData.inValues.stream().collect(
                        Collectors.toMap({ v -> v.attribute }, { v -> v.value })
                    )
                )
            )
        )

    @GetMapping("/items/{id}")
    fun item(@PathVariable id: String): ResponseEntity<ItemProjection> {
        val item = itemFacade.findById(ItemAppId(id))
        return if (item != null) {
            ResponseEntity.ok(toProjection(item))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/items/{id}")
    fun updateItem(
        @PathVariable id: String,
        @RequestBody body: UpdateItemRequest
    ): ResponseEntity<Any> {

        itemFacade.updateItem(
            id = ItemAppId(id),
            manufacturer = body.manufacturer?.let { toManufacturer(body.manufacturer) },
            inValues = body.inValues.stream().collect(Collectors.toMap({ it.attribute }, { it.value }))
        )
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/items/{id}")
    fun deleteItem(@PathVariable id: String): ResponseEntity<Any> {
        itemFacade.deleteItem(ItemAppId(id))
        return ResponseEntity.ok().build()
    }

    private fun toManufacturer(data: ManufacturerProjection) = Manufacturer(
        id = data.id?.let { ManufacturerAppId(data.id) } ?: emptyManufacturerId,
        name = data.name
    )

    private fun toManufacturerData(manufacturer: Manufacturer?) = manufacturer?.let {
        ManufacturerProjection(
            id = manufacturer.id.asAppId().id,
            name = manufacturer.name
        )
    }

    private fun toItemClassIdProjection(id: ItemClassId) = ItemClassIdProjection(
        id = id.asAppId().id,
        version = id.asAppId().version
    )

    private fun toProjection(item: Item) = ItemProjection(
        id = item.id.asAppId().id,
        name = item.name,
        itemClassId = toItemClassIdProjection(item.itemClassId),
        manufacturer = toManufacturerData(item.manufacturer),
        manufacturersCode = item.manufacturersCode,
        values = item.values.map { value ->
            val type = value.attribute.type
            when (value) {
                is ScalarValue -> {
                    if (type is ScalarType) {
                        ScalarProjection(
                            name = value.attribute.name,
                            value = value.value,
                            unit = type.unit
                        )
                    } else {
                        throw InvalidDataException("Illegal type $type.")
                    }
                }
                is DictionaryValue -> {
                    if (type is DictionaryType) {
                        DictionaryProjection(
                            name = value.attribute.name,
                            value = value.value,
                            items = type.items
                        )
                    } else {
                        throw InvalidDataException("Illegal type $type.")
                    }
                }
                else -> throw InvalidDataException("Unknown value: $value.")
            }
        }
    )
}
