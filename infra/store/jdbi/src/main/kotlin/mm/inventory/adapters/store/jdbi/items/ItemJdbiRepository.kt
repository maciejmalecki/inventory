package mm.inventory.adapters.store.jdbi.items

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.adapters.store.jdbi.itemclasses.asJdbiId
import mm.inventory.adapters.store.jdbi.itemclasses.createItemClassId
import mm.inventory.domain.items.item.DictionaryValue
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemRepository
import mm.inventory.domain.items.item.ScalarValue
import mm.inventory.domain.items.item.UpdateValuesCommand
import mm.inventory.domain.items.item.parse
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.shared.types.ItemId
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

class ItemJdbiRepository(private val db: Jdbi, private val itemClassRepository: ItemClassRepository) : ItemRepository {

    override fun findById(id: ItemId): Item? = db.withHandle<Item?, RuntimeException> { handle ->

        val itemDao = handle.attach(ItemDao::class.java)

        val itemRec = itemDao.selectItem(id.asJdbiId().id)
            ?: return@withHandle null
        val itemClass = itemClassRepository.get(createItemClassId(itemRec.itemClassName, itemRec.itemClassVersion))

        val scalarValues = itemDao.selectScalars(id.asJdbiId().id).map {
            val attribute = itemClass.getAttribute(it.attributeType)
            ScalarValue(attribute, it.value!!, it.scale)
        }.toSet()

        val dictionaryValues = itemDao.selectDictionaryValues(id.asJdbiId().id).map {
            val attribute = itemClass.getAttribute(it.attributeType)
            val code = it.code!!
            attribute.parse(code)
        }.toSet()

        Item(
            id = JdbiItemId(itemRec.name),
            name = itemRec.name,
            itemClassId = itemClass.id,
            values = (scalarValues union dictionaryValues).toImmutableSet()
        )
    }

    override fun persist(item: Item): Item = db.inTransaction<Item, RuntimeException> { handle ->
        if (!item.id.empty) {
            throw IllegalArgumentException("The item ${item.id} cannot be persisted, because of nonempty id.")
        }

        val itemDao = handle.attach(ItemDao::class.java)

        // insert item record
        val itemId = createItemId(item.name)
        itemDao.insertItem(ItemRec(item.name, item.itemClassId.asJdbiId().id, item.itemClassId.asJdbiId().version))
        // insert values
        item.values.forEach { value ->
            when (value) {
                is ScalarValue -> itemDao.insertValue(
                    ScalarValueRec(
                        itemName = item.name,
                        attributeType = value.attribute.name,
                        itemClassName = item.itemClassId.asJdbiId().id,
                        itemClassVersion = item.itemClassId.asJdbiId().version,
                        value = value.data,
                        scale = value.scale
                    )
                )
                is DictionaryValue -> itemDao.insertValue(
                    DictionaryValueRec(
                        itemName = item.name,
                        attributeType = value.attribute.name,
                        itemClassName = item.itemClassId.asJdbiId().id,
                        itemClassVersion = item.itemClassId.asJdbiId().version,
                        attributeTypeName = value.attribute.name,
                        code = value.data
                    )
                )
                else -> throw RuntimeException("Unknown value type: ${value.javaClass.name}.")
            }
        }
        return@inTransaction item.copy(id = itemId)
    }

    override fun save(item: Item) = item.handleAll { command ->
        when (command) {
            is UpdateValuesCommand -> updateValues(command)
            else -> throw IllegalArgumentException("Unknown command: ${command.javaClass.name}.")
        }
    }

    override fun delete(item: Item) = db.useTransaction<RuntimeException> { handle ->
        if (item.id.empty) {
            throw IllegalArgumentException("The item ${item.id} cannot be deleted, because of empty id.")
        }
        val dao = handle.attach(ItemDao::class.java)
        val id = item.id.asJdbiId().id
        dao.deleteDictionaryValues(id)
        dao.deleteScalars(id)
        dao.deleteItem(id)
    }

    private fun updateValues(command: UpdateValuesCommand) =
        db.useTransaction<RuntimeException> { handle ->
            command.values.forEach { value ->
                when (value) {
                    is ScalarValue -> updateValue(handle, command.base, value)
                    is DictionaryValue -> updateValue(handle, command.base, value)
                    else -> throw IllegalArgumentException("Unsupported value type: ${value.javaClass.name}.")
                }
            }
        }

    private fun updateValue(handle: Handle, item: Item, value: ScalarValue) {
        val itemDao = handle.attach(ItemDao::class.java)
        val cnt = itemDao.updateValue(
            ScalarValueRec(
                itemName = item.name,
                attributeType = value.attribute.name,
                itemClassName = item.itemClassId.asJdbiId().id,
                itemClassVersion = item.itemClassId.asJdbiId().version,
                value = value.data,
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
                itemClassName = item.itemClassId.asJdbiId().id,
                itemClassVersion = item.itemClassId.asJdbiId().version,
                attributeTypeName = value.attribute.name,
                code = value.data
            )
        )
        if (cnt != 1) {
            throw RuntimeException("Wrong modification count: $cnt.")
        }
    }

}