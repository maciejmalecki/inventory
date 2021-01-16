package mm.inventory.app.productplanner.itemclass

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.app.productplanner.ITEMS_ROLE
import mm.inventory.app.productplanner.ITEM_CLASSES_ROLE
import mm.inventory.domain.items.itemclass.ItemClass
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.types.ItemClassId

/**
 * Facade for ItemClass application component.
 */
class ItemClassFacade(
    private val sec: SecurityGuard,
    private val itemClassRepository: ItemClassRepository,
    private val itemClassQuery: ItemClassQuery,
    private val itemClassIdConverter: ItemClassIdConverter
) {
    fun findAll(): ImmutableList<ItemClassHeader> = sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE) {
        itemClassQuery.findAll()
    }

    fun findById(id: ItemClassId): ItemClass? = sec.requireAllRoles(ITEMS_ROLE, ITEM_CLASSES_ROLE) {
        itemClassRepository.findById(id)
    }

    fun fromItemClassId(id: ItemClassId) = itemClassIdConverter.fromItemClassId(id)

    fun toItemClassId(id: String, version: Long = -1L) = itemClassIdConverter.toItemClassId(id, version)
}