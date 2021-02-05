package mm.inventory.domain.items.itemclass

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.mutations.Mutable
import mm.inventory.domain.shared.mutations.MutatingCommand
import mm.inventory.domain.shared.types.CategoryId

data class DraftItemClass(val itemClass: ItemClass) {
    fun mutable() = MutableDraftItemClass(this)
}

class MutableDraftItemClass(_snapshot: DraftItemClass) : Mutable<DraftItemClass>(_snapshot) {

    val itemClass: ItemClass
        get() = snapshot.itemClass

    fun changeDescription(value: String): MutableDraftItemClass {
        append(
            ChangeDescriptionCommand(snapshot, value),
            snapshot.copy(itemClass = snapshot.itemClass.copy(description = value))
        )
        return this
    }

    fun changeAmountUnit(value: UnitOfMeasurement): MutableDraftItemClass {
        append(
            ChangeAmountUnitCommand(snapshot, value),
            snapshot.copy(itemClass = snapshot.itemClass.copy(amountUnit = value))
        )
        return this
    }

    fun addAttribute(value: Attribute): MutableDraftItemClass {
        append(
            AddAttributeCommand(snapshot, value), snapshot.copy(
                itemClass = snapshot.itemClass.copy(
                    attributes = (snapshot.itemClass.attributes + value).toImmutableSet()
                ),
            )
        )
        return this
    }

    fun removeAttribute(value: Attribute): MutableDraftItemClass {
        append(
            RemoveAttributeCommand(snapshot, value), snapshot.copy(
                itemClass = snapshot.itemClass.copy(
                    attributes = (snapshot.itemClass.attributes - value).toImmutableSet()
                )
            )
        )
        return this
    }

    fun addProposedCategory(value: CategoryId): MutableDraftItemClass {
        if (snapshot.itemClass.proposedCategories.contains(value)) {
            throw InvalidDataException("The $value is already assigned to the ${snapshot.itemClass.id}.")
        }
        append(
            AddProposedCategoryCommand(snapshot, value), snapshot.copy(
                itemClass = snapshot.itemClass.copy(
                    proposedCategories = (snapshot.itemClass.proposedCategories + value).toImmutableSet()
                )
            )
        )
        return this
    }

    fun removeProposedCategory(value: CategoryId): MutableDraftItemClass {
        if (!snapshot.itemClass.proposedCategories.contains(value)) {
            throw InvalidDataException("The $value is not assigned to the ${snapshot.itemClass.id}.")
        }
        append(
            RemoveProposedCategoryCommand(snapshot, value), snapshot.copy(
                itemClass = snapshot.itemClass.copy(
                    proposedCategories = (snapshot.itemClass.proposedCategories - value).toImmutableSet()
                )
            )
        )
        return this
    }
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

data class AddProposedCategoryCommand(
    override val base: DraftItemClass, val category: CategoryId
) : MutatingCommand<DraftItemClass>

data class RemoveProposedCategoryCommand(
    override val base: DraftItemClass, val category: CategoryId
) : MutatingCommand<DraftItemClass>