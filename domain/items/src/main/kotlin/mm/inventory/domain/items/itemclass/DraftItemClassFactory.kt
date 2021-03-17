package mm.inventory.domain.items.itemclass

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.shared.transactions.BusinessTransaction
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.emptyItemClassId

class DraftItemClassFactory(
    private val tx: BusinessTransaction,
    private val itemClassRepository: ItemClassRepository,
    private val draftItemClassRepository: DraftItemClassRepository
) {
    /**
     * Creates new Item Class as draft version.
     * @param name of the item class (must be unique)
     * @param amountUnitOfMeasurement unit of measurement of the item class
     * @return created and already persisted DraftItemClass
     */
    fun createDraft(name: String, amountUnitOfMeasurement: UnitOfMeasurement): DraftItemClass = tx.inTransaction {
        val emptyItemClass = ItemClass(
            id = emptyItemClassId,
            name = name,
            description = "",
            amountUnit = amountUnitOfMeasurement,
            attributes = emptySet<Attribute>().toImmutableSet(),
            proposedCategories = emptySet()
        )
        return@inTransaction draftItemClassRepository.persist(DraftItemClass(emptyItemClass))
    }

    /**
     * Creates new draft version of the existing Item Class. No error is thrown if draft version already
     * exist - the one will be returned.
     * @param itemClassId id of the item class
     * @return draft item class
     */
    fun newDraft(itemClassId: ItemClassId): DraftItemClass = tx.inTransaction {
        val draft = draftItemClassRepository.findById(itemClassId)
        return@inTransaction draft ?: draftItemClassRepository.persist(
            DraftItemClass(
                itemClassRepository.get(itemClassId)
            )
        )
    }
}