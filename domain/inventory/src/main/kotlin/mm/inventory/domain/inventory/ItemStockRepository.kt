package mm.inventory.domain.inventory

import java.math.BigDecimal

data class BookingCapability(val booking: Booking, val availableAmount: BigDecimal) {
    val bookable = booking.amount <= availableAmount
}

interface ItemStockRepository {
    /**
     * Finds stock data for given item.
     */
    fun findByItem(itemCode: String): ItemStock

    /**
     * Replenishes stock for given item.
     */
    fun replenish(itemCode: String, amount: BigDecimal)

    /**
     * Creates new booking for given item.
     */
    fun book(productionRunId: String, itemCode: String, amount: BigDecimal): BookingCapability

    /**
     * Removes booking from booking list and releases resources.
     */
    fun cancel(booking: Booking)

    /**
     * Deducts booking, that is, removes booking and consumes resources.
     */
    fun deduct(booking: Booking)
}