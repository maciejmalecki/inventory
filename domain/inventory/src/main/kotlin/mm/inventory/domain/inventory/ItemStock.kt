package mm.inventory.domain.inventory

import kotlinx.collections.immutable.ImmutableSet
import java.math.BigDecimal
import java.time.LocalDateTime

data class ItemStock(
    val itemCode: String,
    val totalAmount: BigDecimal,
    val bookings: ImmutableSet<Booking>
) {

    val totalBooked = bookings.sumOf { booking -> booking.amount }
    val freeAmount = totalAmount - totalBooked
}

data class Booking(
    val bookingId: String,
    val productionRunId: String,
    val createdAt: LocalDateTime,
    val itemCode: String,
    val amount: BigDecimal)
