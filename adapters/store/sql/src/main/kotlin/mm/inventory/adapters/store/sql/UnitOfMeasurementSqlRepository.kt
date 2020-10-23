package mm.inventory.adapters.store.sql

import io.r2dbc.client.R2dbc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mm.inventory.domain.itemclasses.UnitOfMeasurement
import mm.inventory.domain.itemclasses.UnitOfMeasurementRepository

class UnitOfMeasurementSqlRepository(private val r: R2dbc) : UnitOfMeasurementRepository {

    override suspend fun findAll(): Flow<UnitOfMeasurement> =
            r.withHandle {
                it.select("select code, name from Units").mapResult {
                    it.map { row, _ ->
                        UnitOfMeasurement(row.get("code", String::class.java)!!, row.get("name", String::class.java)!!)
                    }
                }
            }.asFlow()

}