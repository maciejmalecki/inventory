package mm.inventory.adapters.store.jdbi.itemclasses

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.adapters.store.jdbi.units.UnitDao
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.items.itemclass.DictionaryItem
import mm.inventory.domain.items.itemclass.DictionaryType
import mm.inventory.domain.items.itemclass.DraftItemClass
import mm.inventory.domain.items.itemclass.ItemClass
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.items.itemclass.ScalarType
import mm.inventory.domain.items.itemclass.UnitOfMeasurement
import mm.inventory.domain.shared.types.ItemClassId
import org.jdbi.v3.core.Jdbi

/**
 * JDBI based implementation of the ItemClassRepository from domain.
 */
class ItemClassJdbiRepository(private val db: Jdbi) : ItemClassRepository {
    override fun findById(id: ItemClassId): ItemClass? =
            db.withHandle<ItemClass?, RuntimeException> { handle ->

                val itemClassDao = handle.attach(ItemClassDao::class.java)
                val unitDao = handle.attach(UnitDao::class.java)

                // load bare item class
                val itemClassRec = itemClassDao.selectItemClassByName(id.asJdbiId().id)
                        ?: return@withHandle null

                // load unit with subsequent SQL (could be also done with SQL JOIN)
                val unitRec = unitDao.findByCode(itemClassRec.unit)
                        ?: throw RuntimeException("Unit for ${itemClassRec.unit} not found")

                // load all attributes for given item class
                val attributeRecList = itemClassDao.selectAttributesWithTypesForItemClass(id.asJdbiId().id)

                // load all relevant dictionary values
                val dictionaryValueRecMap = itemClassDao.selectAttributeValuesForItemClass(id.asJdbiId().id).groupBy { it.attributeTypeName }

                // build up the aggregate out of fetched data
                ItemClass(
                        JdbiItemClassId(itemClassRec.name),
                        itemClassRec.name,
                        itemClassRec.description,
                        UnitOfMeasurement(unitRec.code, unitRec.name),
                        attributeRecList.map(map(dictionaryValueRecMap)).toImmutableSet())
            }

    // TODO temporary implementation
    override fun findDraftById(id: ItemClassId): DraftItemClass? {
        val itemClass = findById(id) ?: return null
        return DraftItemClass(itemClass)
    }

    override fun persist(draftItemClass: DraftItemClass): DraftItemClass {
        TODO("Not yet implemented")
    }

    override fun save(draftItemClass: DraftItemClass) {
        TODO("Not yet implemented")
    }

    override fun delete(draftItemClass: DraftItemClass) {
        TODO("Not yet implemented")
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