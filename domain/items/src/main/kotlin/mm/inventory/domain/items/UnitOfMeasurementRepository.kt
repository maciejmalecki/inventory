package mm.inventory.domain.items

import kotlinx.collections.immutable.ImmutableList

interface UnitOfMeasurementRepository {
    fun findAll(): ImmutableList<UnitOfMeasurement>
}