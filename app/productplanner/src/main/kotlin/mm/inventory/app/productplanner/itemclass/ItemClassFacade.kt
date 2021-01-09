package mm.inventory.app.productplanner.itemclass

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.domain.items.ITEMS_ROLE
import mm.inventory.domain.items.ITEM_CLASSES_ROLE
import mm.inventory.domain.items.ITEM_CLASSES_WRITER_ROLE
import mm.inventory.domain.items.itemclass.DraftItemClass
import mm.inventory.domain.items.itemclass.DraftItemClassFactory
import mm.inventory.domain.items.itemclass.DraftItemClassManager
import mm.inventory.domain.items.itemclass.DraftItemClassRepository
import mm.inventory.domain.items.itemclass.ItemClass
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.items.itemclass.UnitOfMeasurement
import mm.inventory.domain.items.itemclass.UnitOfMeasurementRepository
import mm.inventory.domain.shared.NotFoundException
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.types.ItemClassId

/**
 * Facade for ItemClass application component.
 */
class ItemClassFacade(
    private val sec: SecurityGuard,
    private val itemClassRepository: ItemClassRepository,
    private val draftItemClassRepository: DraftItemClassRepository,
    private val draftItemClassFactory: DraftItemClassFactory,
    private val draftItemClassManager: DraftItemClassManager,
    private val itemClassQuery: ItemClassQuery,
    private val unitOfMeasurementRepository: UnitOfMeasurementRepository
) {
    fun findAll(): ImmutableList<ItemClassHeader> = sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE) {
        itemClassQuery.findAll()
    }

    fun findById(id: ItemClassId): ItemClass? = sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE) {
        itemClassRepository.findById(id)
    }

    fun findDraftById(id: ItemClassId): DraftItemClass? = sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE) {
        draftItemClassRepository.findById(id)
    }

    fun updateDraft(id: ItemClassId, description: String?, unitCode: String?) =
        sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE, ITEM_CLASSES_WRITER_ROLE) {
            val draftItemClass = draftItemClassRepository.findById(id)
                ?: throw NotFoundException("Draft item class for $id.")
            if (description != null) {
                draftItemClass.changeDescription(description)
            }
            if (unitCode != null) {
                val unit = unitOfMeasurementRepository.get(unitCode)
                draftItemClass.changeAmountUnit(unit)
            }
            if (draftItemClass.hasMutations) {
                draftItemClassRepository.save(draftItemClass)
            }
        }

    fun createDraft(id: ItemClassId): DraftItemClass =
        sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE, ITEM_CLASSES_WRITER_ROLE) {
            draftItemClassFactory.newDraft(id)
        }

    fun createDraft(name: String, unitCode: String): DraftItemClass =
        sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE, ITEM_CLASSES_WRITER_ROLE) {
            draftItemClassFactory.createDraft(name, unitOfMeasurementRepository.get(unitCode))
        }

    fun completeDraft(id: ItemClassId): ItemClass =
        sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE, ITEM_CLASSES_WRITER_ROLE) {
            val draftItemClass =
                draftItemClassRepository.findById(id) ?: throw NotFoundException("Draft item class for $id not found.")
            draftItemClassManager.completeDraft(draftItemClass)
            return@requireAllRoles itemClassRepository.get(draftItemClass.itemClass.id)
        }

    fun rejectDraft(id: ItemClassId) =
        sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE, ITEM_CLASSES_WRITER_ROLE) {
            val draftItemClass =
                draftItemClassRepository.findById(id) ?: throw NotFoundException("Draft item class for $id not found.")
            draftItemClassManager.rejectDraft(draftItemClass)
        }
}