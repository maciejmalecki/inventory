package mm.inventory.domain.inventory

import java.math.BigDecimal

data class Reservation (
    val itemCode: String,
    val reservedAmount: BigDecimal,
    val bookedAmount: BigDecimal
)