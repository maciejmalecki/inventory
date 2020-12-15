package mm.inventory.adapters.store.jdbi.items

import mm.inventory.domain.items.DictionaryValue
import mm.inventory.domain.items.Item
import mm.inventory.domain.items.ItemMutator
import mm.inventory.domain.items.ScalarValue
import org.jdbi.v3.core.Jdbi

class ItemJdbiMutator(private val db: Jdbi) : ItemMutator {

    override fun persist(item: Item) = db.useTransaction<RuntimeException> { handle ->

        val itemDao = handle.attach(ItemDao::class.java)

        // insert item record
        itemDao.insertItem(ItemRec(item.name, item.itemClass.name))
        // insert values
        item.values.forEach { value ->
            when (value) {
                is ScalarValue -> itemDao.insertValue(
                    ScalarValueRec(
                        itemName = item.name,
                        attributeType = value.attribute().name,
                        itemClassName = item.itemClass.name,
                        value = value.getValue(),
                        scale = value.scale
                    )
                )
                is DictionaryValue -> itemDao.insertValue(
                    DictionaryValueRec(
                        itemName = item.name,
                        attributeType = value.attribute().name,
                        itemClassName = item.itemClass.name,
                        attributeTypeName = value.attribute().name,
                        code = value.getValue()
                    )
                )
                else -> throw RuntimeException("Unknown value type: ${value.javaClass.name}.")
            }
        }
    }

    override fun updateValue(item: Item, value: ScalarValue) =
        db.useHandle<RuntimeException> { handle ->
            val itemDao = handle.attach(ItemDao::class.java)
            val cnt = itemDao.updateValue(
                ScalarValueRec(
                    itemName = item.name,
                    attributeType = value.attribute.name,
                    itemClassName = item.itemClass.name,
                    value = value.getValue(),
                    scale = value.scale
                )
            )
            if (cnt != 1) {
                throw RuntimeException("Wrong modification count: $cnt.")
            }
        }

    override fun updateValue(item: Item, value: DictionaryValue) =
        db.useHandle<RuntimeException> { handle ->
            val itemDao = handle.attach(ItemDao::class.java)
            val cnt = itemDao.updateValue(
                DictionaryValueRec(
                    itemName = item.name,
                    attributeType = value.attribute.name,
                    itemClassName = item.itemClass.name,
                    attributeTypeName = value.attribute.name,
                    code = value.getValue()
                )
            )
            if (cnt != 1) {
                throw RuntimeException("Wrong modification count: $cnt.")
            }
        }
}