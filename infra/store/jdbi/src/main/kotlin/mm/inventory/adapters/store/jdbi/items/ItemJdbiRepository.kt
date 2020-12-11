package mm.inventory.adapters.store.jdbi.items

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.ItemClassRepository
import mm.inventory.domain.items.DictionaryValue
import mm.inventory.domain.items.Item
import mm.inventory.domain.items.ItemRepository
import mm.inventory.domain.items.ScalarValue
import org.jdbi.v3.core.Jdbi

class ItemJdbiRepository(private val db: Jdbi, private val itemClassRepository: ItemClassRepository) : ItemRepository {

    override fun findByName(name: String): Item? = db.withHandle<Item?, RuntimeException> { handle ->

        val itemDao = handle.attach(ItemDao::class.java)

        val itemRec = itemDao.selectItem(name)
            ?: return@withHandle null
        val itemClass = itemClassRepository.findByName(itemRec.itemClassName)
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