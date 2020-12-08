package mm.inventory.domain.itemclasses

interface ItemClassRepository {
    fun findByName(name: String): ItemClass?
    fun get(name: String): ItemClass = findByName(name) ?: throw RuntimeException("Item class $name not found.")
    fun findByName(name: String, version: Int): ItemClassVersion?
}