package mm.inventory.adapters.store.jdbi.items

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.itemclasses.ItemClassRepository
import mm.inventory.domain.items.DictionaryValue
import mm.inventory.domain.items.Item
import mm.inventory.domain.items.ItemRepository
import mm.inventory.domain.items.ScalarValue
import org.jdbi.v3.core.Jdbi

class ItemJdbiRepository(private val db: Jdbi, private val itemClassRepository: ItemClassRepository) : ItemRepository {

    override fun load(name: String): Item? = db.withHandle<Item?, RuntimeException> { handle ->

        val itemDao = handle.attach(ItemDao::class.java)

        val itemRec = itemDao.selectItem(name)
            ?: return@withHandle null
        val itemClass = itemClassRepository.findByName(itemRec.itemClassName)
            ?: throw RuntimeException("Item Class for name ${itemRec.itemClassName} not found.")

        val scalarValues = itemDao.selectScalars(name).map {
            val attribute = itemClass.findAttribute(it.attributeType)
                ?: throw RuntimeException("Attribute type ${it.attributeType} does not exist.")
            ScalarValue(attribute, it.value!!, it.scale)
        }.toSet()

        val dictionaryValues = itemDao.selectDictionaryValues(name).map {
            val attribute = itemClass.findAttribute(it.attributeType)
                ?: throw RuntimeException("Attribute type ${it.attributeType} does not exist.")
            val code = it.code!!
            if (attribute.type.isValid(code)) {
                DictionaryValue(attribute, code)
            } else {
                throw RuntimeException("Unknown dictionary code: $code for dictionary: ${attribute.name}.")
            }
        }.toSet()

        Item(
            name = itemRec.name,
            itemClass = itemClass,
            values = (scalarValues union dictionaryValues).toImmutableSet()
        )
    }

    override fun store(item: Item) = db.useTransaction<RuntimeException> { handle ->

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