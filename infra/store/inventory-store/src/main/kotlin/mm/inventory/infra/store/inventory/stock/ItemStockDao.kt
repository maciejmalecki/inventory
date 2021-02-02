package mm.inventory.infra.store.inventory.stock

import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.app.productplanner.stock.ItemStockAppId
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.math.BigDecimal

interface ItemStockDao {
    @SqlUpdate("INSERT INTO Item_Stock (item_name, serial, amount) VALUES (:id.itemId.id, :id.serial+1, :amount)")
    fun insertItemStock(id: ItemStockAppId, amount: BigDecimal): Int

    @SqlQuery("SELECT item_name, SUM(amount) as amount, MAX(serial) as serial FROM Item_Stock WHERE item_name=:id.id GROUP BY item_name")
    fun selectStockAmount(id: ItemAppId): ItemStockRec?
}

data class ItemStockRec(
    val itemName: String,
    val amount: BigDecimal,
    val serial: Int
)