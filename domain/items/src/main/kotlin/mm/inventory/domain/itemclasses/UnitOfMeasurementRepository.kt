package mm.inventory.domain.itemclasses

import kotlinx.collections.immutable.ImmutableList

interface UnitOfMeasurementRepository {
    suspend fun findAll(): ImmutableList<UnitOfMeasurement>
}