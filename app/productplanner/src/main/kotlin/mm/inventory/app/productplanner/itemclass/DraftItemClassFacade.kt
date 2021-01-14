package mm.inventory.app.productplanner.itemclass

import mm.inventory.app.productplanner.ITEMS_ROLE
import mm.inventory.app.productplanner.ITEM_CLASSES_ROLE
import mm.inventory.app.productplanner.ITEM_CLASSES_WRITER_ROLE
import mm.inventory.domain.items.itemclass.Attribute
import mm.inventory.domain.items.itemclass.AttributeTypeRepository
import mm.inventory.domain.items.itemclass.DraftItemClass
import mm.inventory.domain.items.itemclass.DraftItemClassFactory
import mm.inventory.domain.items.itemclass.DraftItemClassManager
import mm.inventory.domain.items.itemclass.DraftItemClassRepository
import mm.inventory.domain.items.itemclass.ItemClass
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.items.itemclass.UnitOfMeasurementRepository
import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.NotFoundException
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction
import mm.inventory.domain.shared.types.ItemClassId

class DraftItemClassFacade(
    private val sec: SecurityGuard,
    private val tx: BusinessTransaction,
    private val draftItemClassRepository: DraftItemClassRepository,
    private val draftItemClassFactory: DraftItemClassFactory,
    private val draftItemClassManager: DraftItemClassManager,
    private val unitOfMeasurementRepository: UnitOfMeasurementRepository,
    private val itemClassRepository: ItemClassRepository,
    private val attributeTypeRepository: AttributeTypeRepository
) {
    fun findDraftById(id: ItemClassId): DraftItemClass? = sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE) {
        draftItemClassRepository.findById(id)
    }

    fun updateDraft(
        id: ItemClassId,
        description: String? = null,
        unitCode: String? = null,
        addedAttributeTypes: List<String> = emptyList(),
        removedAttributeTypes: List<String> = emptyList()
    ) =
        sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE, ITEM_CLASSES_WRITER_ROLE) {
            tx.inTransaction {
                val draftItemClass = draftItemClassRepository.findById(id)
                    ?: throw NotFoundException("Draft item class for $id.")
                if (description != null) {
                    draftItemClass.changeDescription(description)
                }
                if (unitCode != null) {
                    val unit = unitOfMeasurementRepository.get(unitCode)
                    draftItemClass.changeAmountUnit(unit)
                }
                addedAttributeTypes.forEach { attrName ->
                    if (draftItemClass.itemClass.hasAttribute(attrName)) {
                        throw InvalidDataException("An attribute $attrName is already assigned to item class $id.")
                    }
                    // it throws NotFound if such attribute type does not exist in the system, it is ok...
                    // TODO however, we have N reads where we can most likely fetch all attributes using IN clause, fix...
                    val attributeType = attributeTypeRepository.get(attrName)
                    draftItemClass.addAttribute(Attribute(attrName, attributeType))
                }
                removedAttributeTypes.forEach { attrName ->
                    // it throws NotFound if attribute does not exist in item class, it is ok...
                    draftItemClass.removeAttribute(draftItemClass.itemClass.getAttribute(attrName))
                }
                if (draftItemClass.hasMutations) {
                    draftItemClassRepository.save(draftItemClass)
                }
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