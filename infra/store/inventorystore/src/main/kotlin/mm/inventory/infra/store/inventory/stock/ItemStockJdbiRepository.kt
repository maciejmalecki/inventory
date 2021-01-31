package mm.inventory.infra.store.inventory.stock

import mm.inventory.domain.inventory.stock.ItemStock
import mm.inventory.domain.inventory.stock.ItemStockRepository
import mm.inventory.domain.shared.types.ItemId
import org.jdbi.v3.core.Jdbi

class ItemStockJdbiRepository(private val db: Jdbi): ItemStockRepository {
    override fun findByItemId(itemId: ItemId): ItemStock {
        TODO("Not yet implemented")
    }

    override fun update(itemStock: ItemStock): ItemStock {
        TODO("Not yet implemented")
    }
}