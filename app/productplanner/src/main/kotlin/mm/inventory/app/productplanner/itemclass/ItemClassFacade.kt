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
    private val itemClassQuery: ItemClassQuery
) {
    fun findAll(): ImmutableList<ItemClassHeader> = sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE) {
        itemClassQuery.findAll()
    }

    fun findById(id: ItemClassId): ItemClass? = sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE) {
        itemClassRepository.findById(id)
    }

}