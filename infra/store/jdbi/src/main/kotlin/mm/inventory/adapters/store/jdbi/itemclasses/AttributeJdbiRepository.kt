package mm.inventory.adapters.store.jdbi.itemclasses

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.items.itemclass.AttributeRepository
import mm.inventory.domain.items.itemclass.DictionaryItem
import mm.inventory.domain.items.itemclass.DictionaryType
import mm.inventory.domain.items.itemclass.ScalarType
import mm.inventory.domain.items.itemclass.UnitOfMeasurement
import org.jdbi.v3.core.Jdbi

class AttributeJdbiRepository(private val db: Jdbi): AttributeRepository {
    override fun findByName(name: String): Attribute? = db.withHandle<Attribute, RuntimeException> { handle ->
        val dao = handle.attach(AttributeTypeDao::class.java)
        val attributeTypeData = dao.selectAttributeType(name)
        return@withHandle if (attributeTypeData.isEmpty()) {
            null
        } else {
            val first = attributeTypeData.first()
            val type = when(first.scalar) {
                true -> ScalarType(UnitOfMeasurement(first.unitCode!!, first.unitName!!))
                false -> DictionaryType(attributeTypeData.map { DictionaryItem(it.code!!, it.value!!) }.toImmutableSet())
            }
            Attribute(first.name, type)
        }
    }
}