package mm.inventory.adapters.store.jdbi.items

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

class ItemClassJdbiRepository(private val db: Jdbi) : ItemClassRepository {
    override suspend fun findByName(name: String): ItemClass =
            db.inTransaction<ItemClass, RuntimeException> { handle ->

                val itemClassDao = handle.attach(ItemClassDao::class.java)
                val unitDao = handle.attach(UnitDao::class.java)

                // load bare item class
                val itemClassRec = itemClassDao.findByName(name)
                        ?: throw RuntimeException("Item class for $name not found")

                // load unit with subsequent SQL (could be also done with JOIN above)
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
                        attributeRecList.map { attributeWithType ->
                            when (attributeWithType.scalar) {
                                true -> Attribute(attributeWithType.name,
                                        ScalarType(
                                                UnitOfMeasurement(attributeWithType.unitCode, attributeWithType.unitName)))
                                false -> Attribute(attributeWithType.name,
                                        DictionaryType(dictionaryValueRecMap.getOrDefault(attributeWithType.attributeType, emptySet())
                                                .map { itemRec ->
                                                    DictionaryItem(itemRec.value)
                                                }.toImmutableSet()))
                            }
                        }.toImmutableSet())
            }

    // TODO temporary implementation
    override suspend fun findByName(name: String, version: Int): ItemClassVersion =
            ItemClassVersion(findByName(name), version)
}