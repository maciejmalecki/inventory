package mm.inventory.domain.itemclasses

interface ItemClassRepository {
    fun findByName(name: String): ItemClass?
    fun findByName(name: String, version: Int): ItemClassVersion?
}