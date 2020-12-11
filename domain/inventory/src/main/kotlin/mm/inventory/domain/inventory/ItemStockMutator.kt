package mm.inventory.domain.inventory

import java.math.BigDecimal

interface ItemStockMutator {
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
    fun deduct(booking: Booking): BigDecimal
}