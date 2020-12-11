package mm.inventory.domain.items

/**
 * Mutating functions of Item Aggregate.
 */
interface ItemMutator {
    /**
     * Persists newly create Item Aggregate.
     * @param item to be persisted
     */
    fun persist(item: Item)
}