package mm.inventory.domain.items.itemclass

import mm.inventory.domain.shared.types.ItemClassId

interface DraftItemClassRepository {

    fun findById(id: ItemClassId): DraftItemClass?

    fun persist(draftItemClass: DraftItemClass): DraftItemClass

    fun save(draftItemClass: MutableDraftItemClass)

    fun delete(draftItemClass: DraftItemClass)

    fun complete(draftItemClass: DraftItemClass)
}