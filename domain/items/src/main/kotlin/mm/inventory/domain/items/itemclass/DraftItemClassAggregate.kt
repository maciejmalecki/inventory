package mm.inventory.domain.items.itemclass

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.shared.mutations.MutatingCommand
import mm.inventory.domain.shared.mutations.MutatingCommandHandler
import mm.inventory.domain.shared.mutations.Mutations

data class DraftItemClass(
    val itemClass: ItemClass,
    internal val mutations: Mutations<DraftItemClass> = Mutations()
) {
    val hasMutations = !mutations.empty

    fun changeDescription(value: String): DraftItemClass =
        copy(
            itemClass = itemClass.copy(description = value),
            mutations = mutations.append(ChangeDescriptionCommand(this, value))
        )

    fun changeAmountUnit(value: UnitOfMeasurement): DraftItemClass =
        copy(
            itemClass = itemClass.copy(amountUnit = value),
            mutations = mutations.append(ChangeAmountUnitCommand(this, value))
        )

    fun addAttribute(value: Attribute): DraftItemClass =
        copy(
            itemClass = itemClass.copy(attributes = (itemClass.attributes + value).toImmutableSet()),
            mutations = mutations.append(AddAttributeCommand(this, value))
        )

    fun removeAttribute(value: Attribute): DraftItemClass =
        copy(
            itemClass = itemClass.copy(attributes = (itemClass.attributes - value).toImmutableSet()),
            mutations = mutations.append(RemoveAttributeCommand(this, value))
        )

    fun handleAll(handler: MutatingCommandHandler<DraftItemClass>) = mutations.handleAll(handler)
}

data class ChangeDescriptionCommand(
    override val base: DraftItemClass, val description: String
) : MutatingCommand<DraftItemClass>

data class ChangeAmountUnitCommand(
    override val base: DraftItemClass, val amountUnit: UnitOfMeasurement
) : MutatingCommand<DraftItemClass>

data class AddAttributeCommand(
    override val base: DraftItemClass, val attribute: Attribute
) : MutatingCommand<DraftItemClass>

data class RemoveAttributeCommand(
    override val base: DraftItemClass, val attribute: Attribute
) : MutatingCommand<DraftItemClass>