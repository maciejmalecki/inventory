package mm.inventory.domain.items.itemclass

import mm.inventory.domain.shared.transactions.BusinessTransaction

class DraftItemClassManager(
    private val tx: BusinessTransaction,
    private val draftItemClassRepository: DraftItemClassRepository
) {
    fun completeDraft(draftItemClass: DraftItemClass) = tx.inTransaction {
        draftItemClassRepository.complete(draftItemClass)
    }

    fun rejectDraft(draftItemClass: DraftItemClass) = tx.inTransaction {
        draftItemClassRepository.delete(draftItemClass)
    }
}
