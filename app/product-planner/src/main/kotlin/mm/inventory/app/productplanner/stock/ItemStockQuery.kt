package mm.inventory.app.productplanner.stock

import java.math.BigDecimal

data class ItemStockHeader(
    val id: ItemStockAppId,
    val itemName: String,
    val manufacturersCode: String?,
    val amount: BigDecimal,
    val unitCode: String,
    val unitName: String
)

interface ItemStockQuery {

    // TODO pagination?
    fun findAllStock(): List<ItemStockHeader>
}