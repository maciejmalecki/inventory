package mm.inventory.domain.items

interface ItemRepository {
    fun findByName(name: String): Item?
}