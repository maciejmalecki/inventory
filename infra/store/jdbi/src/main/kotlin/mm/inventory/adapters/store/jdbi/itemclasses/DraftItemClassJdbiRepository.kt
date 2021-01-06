package mm.inventory.adapters.store.jdbi.itemclasses

import mm.inventory.domain.items.itemclass.DraftItemClass
import mm.inventory.domain.items.itemclass.DraftItemClassRepository
import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.mutations.Mutations
import mm.inventory.domain.shared.types.ItemClassId
import org.jdbi.v3.core.Jdbi

class DraftItemClassJdbiRepository(private val db: Jdbi) : DraftItemClassRepository {

    override fun findById(id: ItemClassId): DraftItemClass? {
        TODO("Not yet implemented")
    }

    override fun persist(draftItemClass: DraftItemClass): DraftItemClass =
        db.inTransaction<DraftItemClass, RuntimeException> { handle ->
            if (draftItemClass.hasMutations) {
                throw InvalidDataException("Draft Item Class aggregate with mutations cannot be persisted.")
            }
            val itemClass = draftItemClass.itemClass
            val itemClassId = itemClass.id.asJdbiId()
            val itemClassDao = handle.attach(ItemClassDao::class.java)
            // lock, update and check next version
            val version = nextVersion(itemClassDao, itemClassId.id)
            val itemClassRec = ItemClassRec(
                name = itemClass.name,
                description = itemClass.description,
                version = version,
                unit = itemClass.amountUnit.code
            )
            // TODO: should we check if there is a draft already?
            // insert draft
            val updateCnt = itemClassDao.insertItemClass(itemClassRec)
            if (updateCnt != 1) {
                throw InvalidDataException("Invalid update count: $updateCnt for Item Class Aggregate ${itemClass.name}/$version")
            }
            return@inTransaction draftItemClass.copy(
                itemClass = itemClass.copy(id = createItemClassId(itemClassId.id, version)),
                mutations = Mutations()
            )
        }

    override fun save(draftItemClass: DraftItemClass) {
        TODO("Not yet implemented")
    }

    override fun delete(draftItemClass: DraftItemClass) {
        TODO("Not yet implemented")
    }

    override fun complete(draftItemClass: DraftItemClass) = db.useTransaction<RuntimeException> { handle ->
        val itemClassId = draftItemClass.itemClass.id.asJdbiId()
        if (draftItemClass.hasMutations) {
            throw InvalidDataException("Cannot complete Draft Item Class aggregate $itemClassId because it has unsaved mutations.")
        }
        val itemClassDao = handle.attach(ItemClassDao::class.java)
        val updateCount = itemClassDao.completeDraftItemClass(itemClassId.id, itemClassId.version)
        if (updateCount != 1) {
            throw InvalidDataException("The Draft Item Class $itemClassId cannot be completed.")
        }
    }

    private fun nextVersion(itemClassDao: ItemClassDao, itemClassName: String): Long {
        val lastVersion = itemClassDao.selectCounter(itemClassName)
        return if (lastVersion == null) {
            itemClassDao.insertCounter(itemClassName)
            1L
        } else {
            itemClassDao.updateCounter(itemClassName, lastVersion + 1)
            lastVersion + 1
        }
    }
}