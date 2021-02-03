package mm.inventory.infra.store.inventory.stock

import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.app.productplanner.stock.ItemStockAppId
import mm.inventory.app.productplanner.stock.ItemStockHeader
import mm.inventory.app.productplanner.stock.ItemStockQuery
import org.jdbi.v3.core.Jdbi

class ItemStockJdbiQuery(private val db: Jdbi) : ItemStockQuery {
    override fun findAllStock(): List<ItemStockHeader> =
        db.withHandle<List<ItemStockHeader>, RuntimeException> { handle ->
            val dao = handle.attach(ItemStockDao::class.java)
            return@withHandle dao.selectStockWithItems().map {
                ItemStockHeader(
                    id = ItemStockAppId(ItemAppId(it.itemName), it.serial),
                    itemName = it.itemName,
                    manufacturersCode = it.manufacturersCode,
                    amount = it.amount,
                    unitCode = it.unitCode,
                    unitName = it.unitName
                )
            }
        }
}