package mm.inventory.domain.itemclasses

interface ItemClassRepository {
    suspend fun findByName(name: String): ItemClass
}