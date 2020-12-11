package mm.inventory.domain.items

import mm.inventory.domain.shared.NotFoundException

interface ItemClassSelector {
    fun findByName(name: String): ItemClass?
    fun get(name: String): ItemClass = findByName(name) ?: throw NotFoundException("Item class $name not found.")
    fun findByName(name: String, version: Int): ItemClassVersion?
}