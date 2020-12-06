package mm.inventory.domain.items

interface ItemRepository {
    fun load(name: String): Item?
    fun store(item: Item)
}