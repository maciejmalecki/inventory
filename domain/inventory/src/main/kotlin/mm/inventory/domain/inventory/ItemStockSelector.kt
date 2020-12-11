package mm.inventory.domain.inventory


interface ItemStockSelector {
    /**
     * Finds stock data for given item.
     */
    fun findByItem(itemCode: String): ItemStock
}