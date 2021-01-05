package mm.inventory.adapters.store.jdbi.itemclasses

import mm.inventory.domain.items.itemclass.DraftItemClass
import mm.inventory.domain.items.itemclass.DraftItemClassRepository
import mm.inventory.domain.shared.types.ItemClassId

class DraftItemClassJdbiRepository: DraftItemClassRepository {

    override fun findById(id: ItemClassId): DraftItemClass? {
        TODO("Not yet implemented")
    }

    override fun persist(draftItemClass: DraftItemClass): DraftItemClass {
        TODO("Not yet implemented")
    }

    override fun save(draftItemClass: DraftItemClass) {
        TODO("Not yet implemented")
    }

    override fun delete(draftItemClass: DraftItemClass) {
        TODO("Not yet implemented")
    }

    override fun complete(draftItemClass: DraftItemClass) {
        TODO("Not yet implemented")
    }
}