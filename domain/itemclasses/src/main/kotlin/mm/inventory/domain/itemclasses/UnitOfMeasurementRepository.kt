package mm.inventory.domain.itemclasses

import kotlinx.coroutines.flow.Flow

interface UnitOfMeasurementRepository {
    suspend fun findAll(): Flow<UnitOfMeasurement>
}