package mm.inventory.adapters.store.sql

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import mm.inventory.domain.itemclasses.UnitOfMeasurement
import mm.inventory.domain.itemclasses.UnitRepository

class UnitRepositorySqlImpl : UnitRepository {
    override suspend fun findAll() = persistentListOf(UnitOfMeasurement("s", "second")).toImmutableList()
}