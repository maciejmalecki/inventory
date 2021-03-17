package mm.inventory.infra.store.jdbi.items

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
            dao.selectAll().map {
                Manufacturer(
                    id = ManufacturerAppId(it.id),
                    name = it.name
                )
            }.toImmutableList()
        }

    override fun findById(id: ManufacturerId): Manufacturer? =
        db.withHandle<Manufacturer?, RuntimeException> { handle ->
            val dao = handle.attach(ManufacturerDao::class.java)
            dao.selectById(id.asAppId().id)?.let { rec ->
                Manufacturer(
                    id = ManufacturerAppId(rec.id),
                    name = rec.name
                )
            }
        }

    override fun persist(manufacturer: Manufacturer): Manufacturer =
        db.inTransaction<Manufacturer, RuntimeException> { handle ->
            val dao = handle.attach(ManufacturerDao::class.java)
            Manufacturer(
                id = ManufacturerAppId(dao.insert(ManufacturerRec(0L, manufacturer.name))),
                name = manufacturer.name
            )
        }
}