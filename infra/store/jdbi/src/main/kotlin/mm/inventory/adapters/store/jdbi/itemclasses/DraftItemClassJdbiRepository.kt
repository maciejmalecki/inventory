package mm.inventory.adapters.store.jdbi.itemclasses

import mm.inventory.adapters.store.updateAndExpect
import mm.inventory.domain.items.itemclass.AddAttributeCommand
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.items.itemclass.ChangeAmountUnitCommand
import mm.inventory.domain.items.itemclass.ChangeDescriptionCommand
import mm.inventory.domain.items.itemclass.DraftItemClass
import mm.inventory.domain.items.itemclass.DraftItemClassRepository
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.items.itemclass.MutableDraftItemClass
import mm.inventory.domain.items.itemclass.RemoveAttributeCommand
import mm.inventory.domain.shared.types.ItemClassId
import org.jdbi.v3.core.Jdbi

class DraftItemClassJdbiRepository(private val db: Jdbi, private val itemClassRepository: ItemClassRepository) :
    DraftItemClassRepository {

    override fun findById(id: ItemClassId): DraftItemClass? =
        db.inTransaction<DraftItemClass, RuntimeException> { handle ->
            val itemClassDao = handle.attach(ItemClassDao::class.java)
            val jdbiId = id.asJdbiId()
            val draftVersion = itemClassDao.selectDraftVersion(jdbiId.id)
            return@inTransaction if (draftVersion == null) {
                null
            } else {
                DraftItemClass(itemClassRepository.get(createItemClassId(jdbiId.id, draftVersion)))
            }
        }

    override fun persist(draftItemClass: DraftItemClass): DraftItemClass =
        db.inTransaction<DraftItemClass, RuntimeException> { handle ->
            val itemClass = draftItemClass.itemClass
            val itemClassId = itemClass.id.asJdbiId()
            val itemClassDao = handle.attach(ItemClassDao::class.java)
            // lock, update and check next version
            val version = nextVersion(itemClassDao, itemClassId.id)
            // TODO: should we check if there is a draft already?
            // insert draft
            updateAndExpect(1) {
                itemClassDao.insertItemClass(
                    ItemClassRec(
                        name = itemClass.name,
                        version = version,
                        description = itemClass.description,
                        unit = itemClass.amountUnit.code
                    )
                )
            }
            // insert attributes
            itemClass.attributes.forEach { attribute ->
                insertAttribute(itemClassDao, itemClassId, version, attribute)
            }
            return@inTransaction draftItemClass.copy(
                itemClass = itemClass.copy(id = createItemClassId(itemClassId.id, version)),
            )
        }

    override fun save(draftItemClass: MutableDraftItemClass) = db.useTransaction<RuntimeException> { handle ->
        val itemClassDao = handle.attach(ItemClassDao::class.java)
        val id = draftItemClass.itemClass.id.asJdbiId()

        draftItemClass.consume { command ->
            when (command) {
                is ChangeDescriptionCommand -> updateAndExpect(1) {
                    itemClassDao.updateDescription(id, command.description)
                }
                is ChangeAmountUnitCommand -> updateAndExpect(1) {
                    itemClassDao.updateUnit(id, command.amountUnit.code)
                }
                is AddAttributeCommand -> updateAndExpect(1) {
                    itemClassDao.insertAttribute(AttributeRec(id.id, id.version, command.attribute.name))
                }
                is RemoveAttributeCommand -> updateAndExpect(1) {
                    itemClassDao.deleteAttribute(AttributeRec(id.id, id.version, command.attribute.name))
                }
                else -> throw IllegalArgumentException("Unknown command: ${command.javaClass.name}.")
            }
        }
    }

    override fun delete(draftItemClass: DraftItemClass) = db.useTransaction<RuntimeException> { handle ->
        val itemClassDao = handle.attach(ItemClassDao::class.java)
        val id = draftItemClass.itemClass.id.asJdbiId()
        updateAndExpect(1) {
            itemClassDao.deleteDraftItemClass(id)
        }
        updateAndExpect(1) {
            itemClassDao.revertCounter(id.id)
        }
    }

    override fun complete(draftItemClass: DraftItemClass) = db.useTransaction<RuntimeException> { handle ->
        val itemClassDao = handle.attach(ItemClassDao::class.java)
        val itemClassId = draftItemClass.itemClass.id.asJdbiId()
        updateAndExpect(1) {
            itemClassDao.completeDraftItemClass(itemClassId)
        }
    }

    private fun insertAttribute(
        itemClassDao: ItemClassDao,
        itemClassId: JdbiItemClassId,
        version: Long,
        attribute: Attribute
    ) {
        updateAndExpect(1) {
            itemClassDao.insertAttribute(AttributeRec(itemClassId.id, version, attribute.name))
        }
    }

    private fun nextVersion(itemClassDao: ItemClassDao, itemClassName: String): Long {
        val lastVersion = itemClassDao.selectCounterAndLock(itemClassName)
        return if (lastVersion == null) {
            itemClassDao.insertCounter(itemClassName)
            1L
        } else {
            itemClassDao.updateCounter(itemClassName, lastVersion + 1)
            lastVersion + 1
        }
    }
}