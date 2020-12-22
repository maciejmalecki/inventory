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
     * Command: update some of the attributes' values for Item Aggregate.
     * @param item aggregate to be updated
     * @param values values of the attributes to be modified
     */
    fun updateValues(item: Item, values: Set<Value<*>>): Item
}