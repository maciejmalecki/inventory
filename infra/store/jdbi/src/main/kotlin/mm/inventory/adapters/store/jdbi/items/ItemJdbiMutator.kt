package mm.inventory.adapters.store.jdbi.items

import mm.inventory.domain.items.DictionaryValue
import mm.inventory.domain.items.Item
import mm.inventory.domain.items.ItemMutator
import mm.inventory.domain.items.ScalarValue
import org.jdbi.v3.core.Jdbi

class ItemJdbiMutator(private val db: Jdbi): ItemMutator {

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
                        value = value.value(),
                        scale = value.scale
                    )
                )
                is DictionaryValue -> itemDao.insertValue(
                    DictionaryValueRec(
                        itemName = item.name,
                        attributeType = value.attribute().name,
                        itemClassName = item.itemClass.name,
                        attributeTypeName = value.attribute().name,
                        code = value.value()
                    )
                )
                else -> throw RuntimeException("Unknown value type: ${value.javaClass.name}.")
            }
        }
    }
}