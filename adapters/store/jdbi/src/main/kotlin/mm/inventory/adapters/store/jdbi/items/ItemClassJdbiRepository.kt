package mm.inventory.adapters.store.jdbi.items

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.adapters.store.jdbi.units.UnitRec
import mm.inventory.domain.itemclasses.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.kotlin.withHandleUnchecked

class ItemClassJdbiRepository(private val db: Jdbi) : ItemClassRepository {
    override suspend fun findByName(name: String): ItemClass =
            db.withHandleUnchecked {
                val itemClass = it.createQuery("SELECT name, description FROM Item_Classes WHERE name=:name")
                        .bind("name", name)
                        .mapTo<ItemClassRec>()
                        .findOne().get()

                val unit = it.createQuery("SELECT code, name FROM Units WHERE name=:name")
                        .bind("name", itemClass.unit)
                        .mapTo<UnitRec>()
                        .findOne().get()

                val attributes = it.createQuery("""SELECT a.name, a.attribute_type, at.scalar, u.code as unit_code, u.name as unit_name
                    |FROM Attributes a JOIN Attribute_Types at ON a.attribute_type = at.name
                    |JOIN Units u ON at.unit = u.code
                    |WHERE item_class_name=:itemClassName""".trimMargin())
                        .bind("itemClassName", itemClass.name)
                        .mapTo<AttributeWithTypeRec>()
                        .list()

                ItemClass(
                        itemClass.name,
                        itemClass.description,
                        UnitOfMeasurement(unit.code, unit.name),
                        attributes.map { attributeWithType ->
                            when (attributeWithType.scalar) {
                                true -> Attribute(attributeWithType.name,
                                        ScalarType(
                                                UnitOfMeasurement(attributeWithType.unitCode, attributeWithType.unitName)))
                                false -> Attribute(attributeWithType.name,
                                        DictionaryType(emptySet<DictionaryItem>().toImmutableSet()))
                            }
                        }.toImmutableSet())
            }

    // TODO temporary implementation
    override suspend fun findByName(name: String, version: Int): ItemClassVersion =
            ItemClassVersion(findByName(name), version)
}