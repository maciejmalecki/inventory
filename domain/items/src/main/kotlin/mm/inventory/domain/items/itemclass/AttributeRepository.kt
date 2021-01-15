package mm.inventory.domain.items.itemclass

import mm.inventory.domain.shared.NotFoundException

interface AttributeRepository {
    fun findByName(name: String): Attribute?

    fun get(name: String): Attribute =
        findByName(name) ?: throw NotFoundException("Attribute type $name does not exist.")
}