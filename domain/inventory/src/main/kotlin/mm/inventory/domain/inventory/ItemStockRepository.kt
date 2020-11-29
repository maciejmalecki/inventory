package mm.inventory.domain.inventory

import java.math.BigDecimal

interface ItemStockRepository {
    fun replenish(itemCode: String, amount: BigDecimal)
    fun reserve(itemCode: String, amount: BigDecimal): Reservation
    fun cancel(reservation: Reservation)
    fun deduct(reservation: Reservation)
}