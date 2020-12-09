package mm.inventory.domain.itemclasses

import mm.inventory.domain.shared.NotFoundException

interface ItemClassRepository {
    fun findByName(name: String): ItemClass?
    fun get(name: String): ItemClass = findByName(name) ?: throw NotFoundException("Item class $name not found.")
    fun findByName(name: String, version: Int): ItemClassVersion?
}