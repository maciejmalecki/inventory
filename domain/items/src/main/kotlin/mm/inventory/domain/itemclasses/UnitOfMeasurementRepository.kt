package mm.inventory.domain.itemclasses

import kotlinx.collections.immutable.ImmutableList

interface UnitOfMeasurementRepository {
    fun findAll(): ImmutableList<UnitOfMeasurement>
}