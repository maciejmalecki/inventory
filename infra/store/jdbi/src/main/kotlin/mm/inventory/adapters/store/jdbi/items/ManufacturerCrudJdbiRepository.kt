package mm.inventory.adapters.store.jdbi.items

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import mm.inventory.app.productplanner.manufacturer.ManufacturerAppId
import mm.inventory.app.productplanner.manufacturer.ManufacturerCrudRepository
import mm.inventory.app.productplanner.manufacturer.asAppId
import mm.inventory.domain.items.manufacturer.Manufacturer
import mm.inventory.domain.shared.types.ManufacturerId
import org.jdbi.v3.core.Jdbi

class ManufacturerCrudJdbiRepository(private val db: Jdbi) : ManufacturerCrudRepository {
    override fun findAll(): ImmutableList<Manufacturer> =
        db.withHandle<ImmutableList<Manufacturer>, RuntimeException> { handle ->
            val dao = handle.attach(ManufacturerDao::class.java)
            dao.selectAll().map { Manufacturer(ManufacturerAppId(it.id), it.name) }.toImmutableList()
        }

    override fun findById(id: ManufacturerId): Manufacturer? =
        db.withHandle<Manufacturer?, RuntimeException> { handle ->
            val dao = handle.attach(ManufacturerDao::class.java)
            dao.selectById(id.asAppId().id)?.let { rec ->
                Manufacturer(ManufacturerAppId(rec.id), rec.name)
            }
        }

    override fun persist(manufacturer: Manufacturer): Manufacturer =
        db.inTransaction<Manufacturer, RuntimeException> { handle ->
            val dao = handle.attach(ManufacturerDao::class.java)
            // TODO maybe we can get rid of this ugly 0L?
            Manufacturer(ManufacturerAppId(dao.insert(ManufacturerRec(0L, manufacturer.name))), manufacturer.name)
        }
}