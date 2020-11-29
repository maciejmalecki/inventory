package mm.inventory.domain.items

interface ItemRepository {
    fun store(item: Item)
}