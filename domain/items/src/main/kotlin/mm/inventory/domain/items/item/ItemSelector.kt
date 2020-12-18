package mm.inventory.domain.items.item

import mm.inventory.domain.shared.NotFoundException

interface ItemSelector {
    fun findByName(name: String): Item?
    fun get(name: String): Item = findByName(name) ?: throw NotFoundException("Item for name $name not found.")
}