package mm.inventory.infra.store.jdbi.itemclasses

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.infra.store.jdbi.units.UnitDao
import mm.inventory.app.productplanner.itemclass.ItemClassAppId
import mm.inventory.app.productplanner.itemclass.asAppId
import mm.inventory.app.productplanner.shared.CategoryAppId
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.items.itemclass.DictionaryItem
import mm.inventory.domain.items.itemclass.DictionaryType
import mm.inventory.domain.items.itemclass.ItemClass
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.items.itemclass.ScalarType
import mm.inventory.domain.items.itemclass.UnitOfMeasurement
import mm.inventory.domain.shared.NotFoundException
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
            val categoriesDao = handle.attach(ProposedCategoriesDao::class.java)
            val jdbiId = resolve(itemClassDao, id.asAppId())

            // load bare item class
            val itemClassRec = itemClassDao.selectItemClassByName(jdbiId.id, jdbiId.version)
                ?: return@withHandle null

            // load unit with subsequent SQL (could be also done with SQL JOIN)
            val unitRec = unitDao.findByCode(itemClassRec.unit)
                ?: throw RuntimeException("Unit for ${itemClassRec.unit} not found")

            // load all attributes for given item class
            val attributeRecList =
                itemClassDao.selectAttributesWithTypesForItemClass(itemClassRec.name, itemClassRec.version)

            // load all relevant dictionary values
            val dictionaryValueRecMap =
                itemClassDao.selectAttributeValuesForItemClass(itemClassRec.name, itemClassRec.version)
                    .groupBy { it.attributeTypeName }

            val categories = categoriesDao.selectForItemClass(jdbiId)

            // build up the aggregate out of fetched data
            ItemClass(
                ItemClassAppId(itemClassRec.name, itemClassRec.version),
                itemClassRec.name,
                itemClassRec.description,
                UnitOfMeasurement(unitRec.code, unitRec.name),
                attributeRecList.map(map(dictionaryValueRecMap)).toImmutableSet(),
                categories.map { CategoryAppId(it) }.toImmutableSet()
            )
        }

    private fun map(dictionaryValueRecMap: Map<String, List<AttributeTypeValueRec>>): (attributeWithType: AttributeWithTypeRec) -> Attribute =
        { attributeWithType ->
            when (attributeWithType.scalar) {
                true ->
                    Attribute(
                        attributeWithType.name,
                        ScalarType(UnitOfMeasurement(attributeWithType.unitCode!!, attributeWithType.unitName!!))
                    )
                false ->
                    Attribute(
                        attributeWithType.name,
                        DictionaryType(
                            dictionaryValueRecMap.getOrDefault(attributeWithType.attributeType, emptySet())
                                .map { itemRec -> DictionaryItem(itemRec.code, itemRec.value) }.toImmutableSet()
                        )
                    )
            }
        }

    private fun resolve(itemClassDao: ItemClassDao, jdbiId: ItemClassAppId): ItemClassAppId =
        if (jdbiId.useNewest) {
            ItemClassAppId(
                jdbiId.id,
                itemClassDao.selectNewestItemClassVersion(jdbiId.id)
                    ?: throw NotFoundException("No ItemClass for ${jdbiId.id} found.")
            )
        } else {
            jdbiId
        }
}