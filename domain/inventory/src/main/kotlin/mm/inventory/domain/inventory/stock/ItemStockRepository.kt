package mm.inventory.domain.inventory.stock

import mm.inventory.domain.shared.types.ItemId

interface ItemStockRepository {

    fun findByItemId(itemId: ItemId): ItemStock

    fun findByItemIds(itemIds: List<ItemId>): List<ItemStock>

    fun update(itemStock: MutableItemStock)
}