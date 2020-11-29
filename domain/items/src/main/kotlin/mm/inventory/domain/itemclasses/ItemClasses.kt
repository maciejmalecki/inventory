package mm.inventory.domain.itemclasses

import kotlinx.collections.immutable.ImmutableSet

/**
 * Class of items - a template allowing creation of items.
 */
data class ItemClass(
        val name: String,
        val description: String,
        val amountUnit: UnitOfMeasurement,
        val attributes: ImmutableSet<Attribute>
)

data class ItemClassVersion(
        val itemClass: ItemClass,
        val version: Int)


