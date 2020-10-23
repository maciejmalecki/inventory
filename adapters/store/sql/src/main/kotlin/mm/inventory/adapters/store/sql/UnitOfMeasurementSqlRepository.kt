package mm.inventory.adapters.store.sql

import io.r2dbc.client.R2dbc
import io.r2dbc.spi.Result
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import mm.inventory.domain.itemclasses.UnitOfMeasurement
import mm.inventory.domain.itemclasses.UnitOfMeasurementRepository
import reactor.core.publisher.Flux

class UnitOfMeasurementSqlRepository(private val r: R2dbc) : UnitOfMeasurementRepository {

    override suspend fun findAll(): Flux<UnitOfMeasurement> =
            r.withHandle {
                it.select("select code, name from Units").mapResult { result ->
                    result.map { row, _ ->
                        UnitOfMeasurement(row.get("code", String::class.java)!!, row.get("name", String::class.java)!!)
                    }
                }
            }

}