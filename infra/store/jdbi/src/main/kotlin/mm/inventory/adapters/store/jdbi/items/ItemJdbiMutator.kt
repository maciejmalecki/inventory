package mm.inventory.adapters.store.jdbi.items

import mm.inventory.adapters.store.jdbi.itemclasses.asJdbiId
import mm.inventory.domain.items.item.DictionaryValue
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemMutator
import mm.inventory.domain.items.item.ItemSelector
import mm.inventory.domain.items.item.ScalarValue
import mm.inventory.domain.items.item.Value
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

class ItemJdbiMutator(private val db: Jdbi, private val itemSelector: ItemSelector) : ItemMutator {

    override fun persist(item: Item): Item = db.inTransaction<Item, RuntimeException> { handle ->
        if (!item.id.empty) {
            throw IllegalArgumentException("The item ${item.id} cannot be persisted, because of nonempty id.")
        }

        val itemDao = handle.attach(ItemDao::class.java)

        // insert item record
        val itemId = createItemId(item.name);
        itemDao.insertItem(ItemRec(item.name, item.itemClassId.asJdbiId().id))
        // insert values
        item.values.forEach { value ->
            when (value) {
                is ScalarValue -> itemDao.insertValue(
                    ScalarValueRec(
                        itemName = item.name,
                        attributeType = value.attribute().name,
                        itemClassName = item.itemClassId.asJdbiId().id,
                        value = value.getValue(),
                        scale = value.scale
                    )
                )
                is DictionaryValue -> itemDao.insertValue(
                    DictionaryValueRec(
                        itemName = item.name,
                        attributeType = value.attribute().name,
                        itemClassName = item.itemClassId.asJdbiId().id,
                        attributeTypeName = value.attribute().name,
                        code = value.getValue()
                    )
                )
                else -> throw RuntimeException("Unknown value type: ${value.javaClass.name}.")
            }
        }
        return@inTransaction item.copy(id = itemId)
    }

    override fun updateValues(item: Item, values: Set<Value<*>>): Item =
        db.inTransaction<Item, RuntimeException> { handle ->
            values.forEach { value ->
                when (value) {
                    is ScalarValue -> updateValue(handle, item, value)
                    is DictionaryValue -> updateValue(handle, item, value)
                    else -> throw IllegalArgumentException("Unsupported value type: ${value.javaClass.name}.")
                }
            }
            // TODO: this can be optimized just to fetch values via ItemDao
            return@inTransaction itemSelector.get(item.id)
        }

    private fun updateValue(handle: Handle, item: Item, value: ScalarValue) {
        val itemDao = handle.attach(ItemDao::class.java)
        val cnt = itemDao.updateValue(
            ScalarValueRec(
                itemName = item.name,
                attributeType = value.attribute.name,
                itemClassName = item.itemClassId.asJdbiId().id,
                value = value.getValue(),
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
                code = value.getValue()
            )
        )
        if (cnt != 1) {
            throw RuntimeException("Wrong modification count: $cnt.")
        }
    }
}