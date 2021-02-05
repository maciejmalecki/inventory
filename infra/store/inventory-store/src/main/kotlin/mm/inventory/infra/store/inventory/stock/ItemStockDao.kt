package mm.inventory.infra.store.inventory.stock

import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.app.productplanner.stock.ItemStockAppId
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.math.BigDecimal

interface ItemStockDao {
    @SqlUpdate("""
        INSERT INTO Item_Stock (item_name, serial, amount) 
        VALUES (:id.itemId.id, :id.serial+1, :amount)""")
    fun insertItemStock(id: ItemStockAppId, amount: BigDecimal): Int

    @SqlQuery("""
        SELECT item_name, SUM(amount) AS amount, MAX(serial) AS serial 
        FROM Item_Stock 
        WHERE item_name=:id.id 
        GROUP BY item_name""")
    fun selectStockAmount(id: ItemAppId): ItemStockRec?

    @SqlQuery("""
        SELECT item_name, SUM(amount) AS amount, MAX(serial) AS serial 
        FROM Item_Stock 
        WHERE item_name IN (<ids>) 
        GROUP BY item_name""")
    fun selectStockAmounts(@BindList("ids") ids: Array<String>): List<ItemStockRec>

    @SqlQuery("""SELECT item_name, MAX(serial) AS serial, manufacturers_code, code AS unit_code, u.name AS unit_name, SUM(amount) AS amount
        FROM Item_Stock its 
            JOIN Items i ON its.item_name = i.name 
            JOIN Item_classes ic ON i.item_class_name = ic.name AND i.item_class_version = ic.version 
            JOIN Units u ON ic.unit = u.code 
        GROUP BY item_name, manufacturers_code, code, u.name
        ORDER BY item_name""")
    fun selectStockWithItems(): List<StockWithItemRec>
}

data class ItemStockRec(
    val itemName: String,
    val amount: BigDecimal,
    val serial: Int
)

data class StockWithItemRec(
    val itemName: String,
    val serial: Int,
    val manufacturersCode: String?,
    val unitCode: String,
    val unitName: String,
    val amount: BigDecimal
)