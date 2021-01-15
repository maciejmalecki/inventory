package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.domain.items.item.Manufacturer

interface ManufacturerCrudRepository {

    fun findAll(): ImmutableList<Manufacturer>

    fun insert(manufacturer: Manufacturer): Manufacturer
}