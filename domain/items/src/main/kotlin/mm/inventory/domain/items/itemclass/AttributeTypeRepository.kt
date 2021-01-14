package mm.inventory.domain.items.itemclass

import mm.inventory.domain.shared.NotFoundException

interface AttributeTypeRepository {
    fun findByName(name: String): AttributeType?

    fun get(name: String): AttributeType =
        findByName(name) ?: throw NotFoundException("Attribute type $name does not exist.")
}