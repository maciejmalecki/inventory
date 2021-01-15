package mm.inventory.app.productplanner.item

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.domain.items.item.Manufacturer
import mm.inventory.domain.shared.NotFoundException
import mm.inventory.domain.shared.types.ManufacturerId

interface ManufacturerCrudRepository {

    fun findAll(): ImmutableList<Manufacturer>

    fun findById(id: ManufacturerId): Manufacturer?

    fun get(id: ManufacturerId): Manufacturer =
        findById(id) ?: throw NotFoundException("Manufacturer $id cannot be found.")

    fun insert(manufacturer: Manufacturer): Manufacturer
}