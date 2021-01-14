package mm.inventory.adapters.store.jdbi.itemclasses

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.itemclass.AttributeType
import mm.inventory.domain.items.itemclass.AttributeTypeRepository
import mm.inventory.domain.items.itemclass.DictionaryItem
import mm.inventory.domain.items.itemclass.DictionaryType
import mm.inventory.domain.items.itemclass.ScalarType
import mm.inventory.domain.items.itemclass.UnitOfMeasurement
import org.jdbi.v3.core.Jdbi

class AttributeTypeJdbiRepository(private val db: Jdbi): AttributeTypeRepository {
    override fun findByName(name: String): AttributeType? = db.withHandle<AttributeType, RuntimeException> { handle ->
        val dao = handle.attach(AttributeTypeDao::class.java)
        val attributeTypeData = dao.selectAttributeType(name)
        return@withHandle if (attributeTypeData.isEmpty()) {
            null
        } else {
            val first = attributeTypeData.first()
            when(first.scalar) {
                true -> ScalarType(UnitOfMeasurement(first.unitCode!!, first.unitName!!))
                false -> DictionaryType(attributeTypeData.map { DictionaryItem(it.code!!, it.value!!) }.toImmutableSet())
            }
        }
    }
}