package mm.inventory.domain.items.itemclass

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.shared.transactions.BusinessTransaction
import mm.inventory.domain.shared.types.ItemClassId
import mm.inventory.domain.shared.types.emptyItemClassId

class DraftItemClassFactory(
    private val tx: BusinessTransaction,
    private val itemClassRepository: ItemClassRepository
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
            attributes = emptySet<Attribute>().toImmutableSet()
        )
        return@inTransaction itemClassRepository.persist(DraftItemClass(emptyItemClass))
    }

    /**
     * Creates new draft version of the existing Item Class. No error is thrown if draft version already
     * exist - the one will be returned.
     * @param itemClassId id of the item class
     * @return draft item class
     */
    fun newDraft(itemClassId: ItemClassId): DraftItemClass = tx.inTransaction {
        val draft = itemClassRepository.findDraftById(itemClassId)
        return@inTransaction draft ?: itemClassRepository.persist(DraftItemClass(itemClassRepository.get(itemClassId)))
    }
}