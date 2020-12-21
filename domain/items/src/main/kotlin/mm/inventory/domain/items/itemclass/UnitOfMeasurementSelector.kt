package mm.inventory.domain.items.itemclass

import kotlinx.collections.immutable.ImmutableList

interface UnitOfMeasurementSelector {
    fun findAll(): ImmutableList<UnitOfMeasurement>
}