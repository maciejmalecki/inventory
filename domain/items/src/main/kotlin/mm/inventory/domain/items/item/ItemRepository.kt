package mm.inventory.domain.items.item

import mm.inventory.domain.shared.NotFoundException
import mm.inventory.domain.shared.types.ItemId

interface ItemRepository {
    fun findById(id: ItemId): Item?
    fun get(id: ItemId): Item = findById(id) ?: throw NotFoundException("Item for name $id not found.")

    /**
     * Persists newly created Item Aggregate.
     * @param item to be persisted
     */
    fun persist(item: Item): Item

    /**
     * Saves aggregate to the store.
     * @param item to be saved
     */
    fun save(item: Item)

    /**
     * Synchronises Item Aggregate with store and returns fresh snapshot.
     * @param item to be saved
     * @return actual Item Aggregate
     */
    fun saveAndGet(item: Item): Item {
        save(item)
        return get(item.id)
    }

    /**
     * Deletes aggregate.
     * @param item to be deleted
     */
    fun delete(item: Item)
}
