package mm.inventory.domain.items.itemclass

import mm.inventory.domain.shared.transactions.BusinessTransaction

class DraftItemClassManager(
    private val tx: BusinessTransaction,
    private val itemClassRepository: ItemClassRepository
) {

    fun completeDraft(draftItemClass: DraftItemClass) {

    }

    fun rejectDraft(draftItemClass: DraftItemClass) = itemClassRepository.delete(draftItemClass)
}