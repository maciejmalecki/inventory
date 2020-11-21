package mm.inventory.domain.inventory

import java.math.BigDecimal

data class ItemStock(
        val amount: BigDecimal,
        val itemCode: String
)