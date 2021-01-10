package mm.inventory.adapters.store.jdbi.units

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import mm.inventory.domain.items.itemclass.UnitOfMeasurement
import mm.inventory.domain.items.itemclass.UnitOfMeasurementRepository
import org.jdbi.v3.core.Jdbi

class UnitOfMeasurementJdbiRepository(private val db: Jdbi) : UnitOfMeasurementRepository {

    override fun findAll(): ImmutableList<UnitOfMeasurement> =
        db.withHandle<ImmutableList<UnitOfMeasurement>, RuntimeException> { handle ->
            handle.attach(UnitDao::class.java).findAll().map { rec ->
                UnitOfMeasurement(rec.code, rec.name)
            }.toImmutableList()
        }

    override fun findByCode(code: String): UnitOfMeasurement? =
        db.withHandle<UnitOfMeasurement?, RuntimeException> { handle ->
            val unitRec = handle.attach(UnitDao::class.java).findByCode(code)
            if (unitRec == null) {
                null
            } else {
                UnitOfMeasurement(unitRec.code, unitRec.name)
            }
        }
}