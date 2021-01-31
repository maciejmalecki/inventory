package mm.inventory.infra.store.jdbi.items

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.infra.store.updateAndExpect
import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.app.productplanner.item.asAppId
import mm.inventory.app.productplanner.manufacturer.asAppId
import mm.inventory.app.productplanner.itemclass.ItemClassAppId
import mm.inventory.app.productplanner.itemclass.asAppId
import mm.inventory.app.productplanner.manufacturer.ManufacturerAppId
import mm.inventory.domain.items.item.DictionaryValue
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemRepository
import mm.inventory.domain.items.manufacturer.Manufacturer
import mm.inventory.domain.items.item.MutableItem
import mm.inventory.domain.items.item.RemoveManufacturerCommand
import mm.inventory.domain.items.item.ScalarValue
import mm.inventory.domain.items.item.UpdateManufacturerCommand
import mm.inventory.domain.items.item.UpdateValuesCommand
import mm.inventory.domain.items.item.parse
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.shared.types.ItemId
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

class ItemJdbiRepository(private val db: Jdbi, private val itemClassRepository: ItemClassRepository) : ItemRepository {

    override fun findById(id: ItemId): Item? = db.withHandle<Item?, RuntimeException> { handle ->

        val itemDao = handle.attach(ItemDao::class.java)

        val itemRec = itemDao.selectItem(id.asAppId().id)
            ?: return@withHandle null
        val itemClass = itemClassRepository.get(ItemClassAppId(itemRec.itemClassName, itemRec.itemClassVersion))

        val scalarValues = itemDao.selectScalars(id.asAppId().id).map {
            val attribute = itemClass.getAttribute(it.attributeType)
            ScalarValue(attribute, it.value!!, it.scale)
        }.toSet()

        val dictionaryValues = itemDao.selectDictionaryValues(id.asAppId().id).map {
            val attribute = itemClass.getAttribute(it.attributeType)
            val code = it.code!!
            attribute.parse(code)
        }.toSet()

        Item(
            id = ItemAppId(itemRec.name),
            name = itemRec.name,
            itemClassId = itemClass.id,
            manufacturer = itemRec.manufacturerId?.let {
                Manufacturer(
                    ManufacturerAppId(itemRec.manufacturerId),
                    itemRec.manufacturerName ?: ""
                )
            },
            manufacturersCode = itemRec.manufacturersCode,
            values = (scalarValues union dictionaryValues).toImmutableSet()
        )
    }

    override fun persist(item: Item): Item = db.inTransaction<Item, RuntimeException> { handle ->
        if (!item.id.empty) {
            throw IllegalArgumentException("The item ${item.id} cannot be persisted, because of nonempty id.")
        }

        val itemDao = handle.attach(ItemDao::class.java)

        // insert item record
        val itemId = ItemAppId(item.name)
        itemDao.insertItem(
            ItemRec(
                name = item.name,
                itemClassName = item.itemClassId.asAppId().id,
                itemClassVersion = item.itemClassId.asAppId().version,
                manufacturerId = item.manufacturer?.id?.asAppId()?.id,
                manufacturersCode = item.manufacturersCode
            )
        )
        // insert values
        item.values.forEach { value ->
            when (value) {
                is ScalarValue -> itemDao.insertValue(
                    ScalarValueRec(
                        itemName = item.name,
                        attributeType = value.attribute.name,
                        itemClassName = item.itemClassId.asAppId().id,
                        itemClassVersion = item.itemClassId.asAppId().version,
                        value = value.value,
                        scale = value.scale
                    )
                )
                is DictionaryValue -> itemDao.insertValue(
                    DictionaryValueRec(
                        itemName = item.name,
                        attributeType = value.attribute.name,
                        itemClassName = item.itemClassId.asAppId().id,
                        itemClassVersion = item.itemClassId.asAppId().version,
                        attributeTypeName = value.attribute.name,
                        code = value.value
                    )
                )
                else -> throw RuntimeException("Unknown value type: ${value.javaClass.name}.")
            }
        }
        return@inTransaction item.copy(id = itemId)
    }

    override fun save(item: MutableItem) = db.useTransaction<RuntimeException> { handle ->
        item.consume { command ->
            when (command) {
                is UpdateValuesCommand -> updateValues(handle, command)
                is UpdateManufacturerCommand -> updateManufacturer(handle, command)
                is RemoveManufacturerCommand -> removeManufacturer(handle, command)
                else -> throw IllegalArgumentException("Unknown command: ${command.javaClass.name}.")
            }
        }
    }

    override fun delete(item: Item) = db.useTransaction<RuntimeException> { handle ->
        if (item.id.empty) {
            throw IllegalArgumentException("The item ${item.id} cannot be deleted, because of empty id.")
        }
        val dao = handle.attach(ItemDao::class.java)
        val id = item.id.asAppId().id
        dao.deleteDictionaryValues(id)
        dao.deleteScalars(id)
        dao.deleteItem(id)
    }

    private fun updateManufacturer(handle: Handle, command: UpdateManufacturerCommand) = updateAndExpect(1) {
        handle.attach(ItemDao::class.java)
            .updateManufacturer(command.manufacturer.id.asAppId(), command.manufacturersCode, command.base.id.asAppId())
    }

    private fun removeManufacturer(handle: Handle, command: RemoveManufacturerCommand) = updateAndExpect(1) {
        handle.attach(ItemDao::class.java).removeManufacturer(command.base.id.asAppId())
    }

    private fun updateValues(handle: Handle, command: UpdateValuesCommand) =
        command.values.forEach { value ->
            when (value) {
                is ScalarValue -> updateValue(handle, command.base, value)
                is DictionaryValue -> updateValue(handle, command.base, value)
                else -> throw IllegalArgumentException("Unsupported value type: ${value.javaClass.name}.")
            }
        }

    private fun updateValue(handle: Handle, item: Item, value: ScalarValue) {
        val itemDao = handle.attach(ItemDao::class.java)
        val cnt = itemDao.updateValue(
            ScalarValueRec(
                itemName = item.name,
                attributeType = value.attribute.name,
                itemClassName = item.itemClassId.asAppId().id,
                itemClassVersion = item.itemClassId.asAppId().version,
                value = value.value,
                scale = value.scale
            )
        )
        if (cnt != 1) {
            throw RuntimeException("Wrong modification count: $cnt.")
        }
    }

    private fun updateValue(handle: Handle, item: Item, value: DictionaryValue) {
        val itemDao = handle.attach(ItemDao::class.java)
        val cnt = itemDao.updateValue(
            DictionaryValueRec(
                itemName = item.name,
                attributeType = value.attribute.name,
                itemClassName = item.itemClassId.asAppId().id,
                itemClassVersion = item.itemClassId.asAppId().version,
                attributeTypeName = value.attribute.name,
                code = value.value
            )
        )
        if (cnt != 1) {
            throw RuntimeException("Wrong modification count: $cnt.")
        }
    }
}
