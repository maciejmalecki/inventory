package mm.inventory.domain.items.item

/**
 * Mutating functions of Item Aggregate.
 */
interface ItemMutator {
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

    /**
     * Command: delete aggregate.
     * @param item to be deleted
     */
    fun delete(item: Item)
}