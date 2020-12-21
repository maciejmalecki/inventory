package mm.inventory.domain.items.itemclass

import mm.inventory.domain.shared.NotFoundException
import mm.inventory.domain.shared.types.ItemClassId

interface ItemClassSelector {
    fun findById(id: ItemClassId): ItemClass?
    fun get(id: ItemClassId): ItemClass = findById(id) ?: throw NotFoundException("Item class $id not found.")
    fun findById(id: ItemClassId, version: Int): ItemClassVersion?
}