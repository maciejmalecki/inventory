package mm.inventory.domain.production

import kotlinx.collections.immutable.ImmutableSet
import mm.inventory.domain.inventory.BookingCapability

data class ProductionBatch(
    val batchNo: Int,
    val projectCode: String,
    val revision: ProjectRevision,
    val batchSize: Int
)

data class ProductionBatchBooking(
    val productionRunId: String,
    val bookings: ImmutableSet<BookingCapability>
) {
    val bookable = bookings.all { it.bookable }
}
