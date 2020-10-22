package mm.inventory.domain.itemclasses

import kotlinx.collections.immutable.ImmutableList

interface UnitRepository {
    suspend fun findAll(): ImmutableList<UnitOfMeasurement>
}