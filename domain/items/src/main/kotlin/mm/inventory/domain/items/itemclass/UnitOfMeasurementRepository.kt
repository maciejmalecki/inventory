package mm.inventory.domain.items.itemclass

import kotlinx.collections.immutable.ImmutableList

interface UnitOfMeasurementRepository {
    fun findAll(): ImmutableList<UnitOfMeasurement>
}