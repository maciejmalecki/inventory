package mm.inventory.domain.items.item

import mm.inventory.domain.shared.NotFoundException
import mm.inventory.domain.shared.types.ItemId

interface ItemRepository {
    fun findById(id: ItemId): Item?
    fun get(id: ItemId): Item = findById(id) ?: throw NotFoundException("Item for name $id not found.")
    /**
     * Command: persist newly created Item Aggregate.
     * @param item to be persisted
     */
    fun persist(item: Item): Item

    /**
     * Command: saves aggregate to the store.
     * @param item to be saved
     */
    fun save(item: Item)

    fun saveAndGet(item: Item): Item {
        save(item)
        return get(item.id)
    }

    /**
     * Command: delete aggregate.
     * @param item to be deleted
     */
    fun delete(item: Item)

}