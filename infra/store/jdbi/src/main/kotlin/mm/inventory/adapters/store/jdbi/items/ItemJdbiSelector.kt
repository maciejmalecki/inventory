package mm.inventory.adapters.store.jdbi.items

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.itemclass.ItemClassSelector
import mm.inventory.domain.items.item.DictionaryValue
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemSelector
import mm.inventory.domain.items.item.ScalarValue
import org.jdbi.v3.core.Jdbi

class ItemJdbiSelector(private val db: Jdbi, private val itemClassSelector: ItemClassSelector) : ItemSelector {

    override fun findByName(name: String): Item? = db.withHandle<Item?, RuntimeException> { handle ->

        val itemDao = handle.attach(ItemDao::class.java)

        val itemRec = itemDao.selectItem(name)
            ?: return@withHandle null
        val itemClass = itemClassSelector.findByName(itemRec.itemClassName)
            ?: throw RuntimeException("Item Class for name ${itemRec.itemClassName} not found.")

        val scalarValues = itemDao.selectScalars(name).map {
            val attribute = itemClass.getAttribute(it.attributeType)
            ScalarValue(attribute, it.value!!, it.scale)
        }.toSet()

        val dictionaryValues = itemDao.selectDictionaryValues(name).map {
            val attribute = itemClass.getAttribute(it.attributeType)
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

}