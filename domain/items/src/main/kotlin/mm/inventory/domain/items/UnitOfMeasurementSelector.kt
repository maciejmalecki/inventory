package mm.inventory.domain.items

import kotlinx.collections.immutable.ImmutableList

interface UnitOfMeasurementSelector {
    fun findAll(): ImmutableList<UnitOfMeasurement>
}