package mm.inventory.domain.items.item

import mm.inventory.domain.shared.NotFoundException
import mm.inventory.domain.shared.types.ItemId

interface ItemSelector {
    fun findById(id: ItemId): Item?
    fun get(id: ItemId): Item = findById(id) ?: throw NotFoundException("Item for name $id not found.")
}