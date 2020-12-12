package mm.inventory.adapters.store.jdbi.itemclasses

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.adapters.store.jdbi.units.UnitDao
import mm.inventory.domain.items.Attribute
import mm.inventory.domain.items.DictionaryItem
import mm.inventory.domain.items.DictionaryType
import mm.inventory.domain.items.ItemClass
import mm.inventory.domain.items.ItemClassSelector
import mm.inventory.domain.items.ItemClassVersion
import mm.inventory.domain.items.ScalarType
import mm.inventory.domain.items.UnitOfMeasurement
import org.jdbi.v3.core.Jdbi

/**
 * JDBI based implementation of the ItemClassRepository from domain.
 */
class ItemClassJdbiSelector(private val db: Jdbi) : ItemClassSelector {
    override fun findByName(name: String): ItemClass? =
            db.withHandle<ItemClass?, RuntimeException> { handle ->

                val itemClassDao = handle.attach(ItemClassDao::class.java)
                val unitDao = handle.attach(UnitDao::class.java)

                // load bare item class
                val itemClassRec = itemClassDao.selectItemClassByName(name)
                        ?: return@withHandle null

                // load unit with subsequent SQL (could be also done with SQL JOIN)
                val unitRec = unitDao.findByCode(itemClassRec.unit)
                        ?: throw RuntimeException("Unit for ${itemClassRec.unit} not found")

                // load all attributes for given item class
                val attributeRecList = itemClassDao.selectAttributesWithTypesForItemClass(name)

                // load all relevant dictionary values
                val dictionaryValueRecMap = itemClassDao.selectAttributeValuesForItemClass(name).groupBy { it.attributeTypeName }

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
                        ScalarType(UnitOfMeasurement(attributeWithType.unitCode!!, attributeWithType.unitName!!))
                )
            false ->
                Attribute(
                        attributeWithType.name,
                        DictionaryType(dictionaryValueRecMap.getOrDefault(attributeWithType.attributeType, emptySet())
                                .map { itemRec -> DictionaryItem(itemRec.code, itemRec.value) }.toImmutableSet())
                )
        }
    }

}