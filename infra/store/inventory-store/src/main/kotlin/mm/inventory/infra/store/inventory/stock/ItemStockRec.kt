package mm.inventory.infra.store.inventory.stock

import java.math.BigDecimal

data class ItemStockRec(
    val itemName: String,
    val amount: BigDecimal,
    val serial: Int
)
