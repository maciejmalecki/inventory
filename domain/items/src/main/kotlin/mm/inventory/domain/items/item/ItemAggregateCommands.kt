package mm.inventory.domain.items.item

import kotlinx.collections.immutable.ImmutableSet

interface ItemCommand {
    val base: Item
}

data class UpdateValuesCommand(
    override val base: Item,
    val values: ImmutableSet<Value<*>>
) : ItemCommand

typealias ItemCommandHandler = (T: ItemCommand) -> Item