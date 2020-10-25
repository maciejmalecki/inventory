package mm.inventory.adapters.store.r2dbc

import io.r2dbc.client.R2dbc
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.reactive.awaitSingle
import mm.inventory.domain.itemclasses.UnitOfMeasurement
import mm.inventory.domain.itemclasses.UnitOfMeasurementRepository

class UnitOfMeasurementR2dbcRepository(private val r: R2dbc) : UnitOfMeasurementRepository {

    override suspend fun findAll(): ImmutableList<UnitOfMeasurement> =
            r.withHandle {
                it.select("select code, name from Units").mapResult {
                    it.map { row, _ ->
                        UnitOfMeasurement(
                                row.get("code") as String,
                                row.get("name") as String)
                    }
                }
            }.collectList().map {
                it.toImmutableList()
            }.awaitSingle()

}