package mm.inventory.adapters.store.jdbi.units

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import mm.inventory.domain.itemclasses.UnitOfMeasurement
import mm.inventory.domain.itemclasses.UnitOfMeasurementRepository
import org.jdbi.v3.core.Jdbi

class UnitOfMeasurementJdbiRepository(private val db: Jdbi) : UnitOfMeasurementRepository {

    override suspend fun findAll(): ImmutableList<UnitOfMeasurement> =
            db.withHandle<ImmutableList<UnitOfMeasurement>, RuntimeException> { handle ->
                handle.attach(UnitDao::class.java).findAll().map { rec ->
                    UnitOfMeasurement(rec.code, rec.name)
                }.toImmutableList()
            }
}