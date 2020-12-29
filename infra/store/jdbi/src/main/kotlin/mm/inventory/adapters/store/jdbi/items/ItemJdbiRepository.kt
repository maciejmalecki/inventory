package mm.inventory.adapters.store.jdbi.items

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.adapters.store.jdbi.itemclasses.asJdbiId
import mm.inventory.adapters.store.jdbi.itemclasses.createItemClassId
import mm.inventory.domain.items.item.DictionaryValue
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemMutator
import mm.inventory.domain.items.item.ItemSelector
import mm.inventory.domain.items.item.ScalarValue
import mm.inventory.domain.items.item.UpdateValuesCommand
import mm.inventory.domain.items.item.Value
import mm.inventory.domain.items.item.parse
import mm.inventory.domain.items.itemclass.ItemClassSelector
import mm.inventory.domain.shared.types.ItemId
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

class ItemJdbiRepository(private val db: Jdbi, private val itemClassSelector: ItemClassSelector) : ItemSelector,
    ItemMutator {

    override fun findById(id: ItemId): Item? = db.withHandle<Item?, RuntimeException> { handle ->

        val itemDao = handle.attach(ItemDao::class.java)

        val itemRec = itemDao.selectItem(id.asJdbiId().id)
            ?: return@withHandle null
        val itemClass = itemClassSelector.get(createItemClassId(itemRec.itemClassName))

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
        itemDao.insertItem(ItemRec(item.name, item.itemClassId.asJdbiId().id))
        // insert values
        item.values.forEach { value ->
            when (value) {
                is ScalarValue -> itemDao.insertValue(
                    ScalarValueRec(
                        itemName = item.name,
                        attributeType = value.attribute.name,
                        itemClassName = item.itemClassId.asJdbiId().id,
                        value = value.value,
                        scale = value.scale
                    )
                )
                is DictionaryValue -> itemDao.insertValue(
                    DictionaryValueRec(
                        itemName = item.name,
                        attributeType = value.attribute.name,
                        itemClassName = item.itemClassId.asJdbiId().id,
                        attributeTypeName = value.attribute.name,
                        code = value.value
                    )
                )
                else -> throw RuntimeException("Unknown value type: ${value.javaClass.name}.")
            }
        }
        return@inTransaction item.copy(id = itemId)
    }

    override fun save(item: Item): Item = item.runMutations { command ->
        when (command) {
            is UpdateValuesCommand -> updateValues(command.base, command.values)
            else -> throw IllegalArgumentException("Unknown command: ${command.javaClass.name}.")
        }
    }

    private fun updateValues(item: Item, values: Set<Value<*>>): Item =
        db.inTransaction<Item, RuntimeException> { handle ->
            values.forEach { value ->
                when (value) {
                    is ScalarValue -> updateValue(handle, item, value)
                    is DictionaryValue -> updateValue(handle, item, value)
                    else -> throw IllegalArgumentException("Unsupported value type: ${value.javaClass.name}.")
                }
            }
            // TODO: this can be optimized just to fetch values via ItemDao
            return@inTransaction get(item.id)
        }

    private fun updateValue(handle: Handle, item: Item, value: ScalarValue) {
        val itemDao = handle.attach(ItemDao::class.java)
        val cnt = itemDao.updateValue(
            ScalarValueRec(
                itemName = item.name,
                attributeType = value.attribute.name,
                itemClassName = item.itemClassId.asJdbiId().id,
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
                itemClassName = item.itemClassId.asJdbiId().id,
                attributeTypeName = value.attribute.name,
                code = value.value
            )
        )
        if (cnt != 1) {
            throw RuntimeException("Wrong modification count: $cnt.")
        }
    }

}