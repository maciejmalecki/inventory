package mm.inventory.adapters.store.jdbi.itemclasses

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.adapters.store.jdbi.units.UnitDao
import mm.inventory.domain.itemclasses.Attribute
import mm.inventory.domain.itemclasses.DictionaryItem
import mm.inventory.domain.itemclasses.DictionaryType
import mm.inventory.domain.itemclasses.ItemClass
import mm.inventory.domain.itemclasses.ItemClassRepository
import mm.inventory.domain.itemclasses.ItemClassVersion
import mm.inventory.domain.itemclasses.ScalarType
import mm.inventory.domain.itemclasses.UnitOfMeasurement
import org.jdbi.v3.core.Jdbi

/**
 * JDBI based implementation of the ItemClassRepository from domain.
 */
class ItemClassJdbiRepository(private val db: Jdbi) : ItemClassRepository {
    override fun findByName(name: String): ItemClass? =
            db.inTransaction<ItemClass?, RuntimeException> { handle ->

                val itemClassDao = handle.attach(ItemClassDao::class.java)
                val unitDao = handle.attach(UnitDao::class.java)

                // load bare item class
                val itemClassRec = itemClassDao.findByName(name)
                        ?: return@inTransaction null

                // load unit with subsequent SQL (could be also done with SQL JOIN)
                val unitRec = unitDao.findByCode(itemClassRec.unit)
                        ?: throw RuntimeException("Unit for ${itemClassRec.unit} not found")

                // load all attributes for given item class
                val attributeRecList = itemClassDao.findAttributesWithTypesForItemClass(name)

                // load all relevant dictionary values
                val dictionaryValueRecMap = itemClassDao.findAttributeValuesForItemClass(name).groupBy { it.attributeTypeName }

                // build up the aggregate out of fetched data
                ItemClass(
                        itemClassRec.name,
                        itemClassRec.description,
                        UnitOfMeasurement(unitRec.code, unitRec.name),
                        attributeRecList.map(map(dictionaryValueRecMap)).toImmutableSet())
            }

    // TODO temporary implementation
    override fun findByName(name: String, version: Int): ItemClassVersion? {
        val itemClass = findByName(name) ?: return null
        return ItemClassVersion(itemClass, version)
    }

    private fun map(dictionaryValueRecMap: Map<String, List<AttributeTypeValueRec>>): (attributeWithType: AttributeWithTypeRec) -> Attribute = { attributeWithType ->
        when (attributeWithType.scalar) {
            true ->
                Attribute(
                        attributeWithType.name,
                        ScalarType(UnitOfMeasurement(attributeWithType.unitCode!!, attributeWithType.unitName!!)))
            false ->
                Attribute(
                        attributeWithType.name,
                        DictionaryType(dictionaryValueRecMap.getOrDefault(attributeWithType.attributeType, emptySet())
                                .map { itemRec -> DictionaryItem(itemRec.code, itemRec.value) }.toImmutableSet()))
        }
    }

}