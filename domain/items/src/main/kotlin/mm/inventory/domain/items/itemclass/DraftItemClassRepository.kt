package mm.inventory.domain.items.itemclass

import mm.inventory.domain.shared.types.ItemClassId

interface DraftItemClassRepository {

    fun findDraftById(id: ItemClassId): DraftItemClass?

    fun persist(draftItemClass: DraftItemClass): DraftItemClass

    fun save(draftItemClass: DraftItemClass)

    fun delete(draftItemClass: DraftItemClass)

    fun complete(draftItemClass: DraftItemClass)
}