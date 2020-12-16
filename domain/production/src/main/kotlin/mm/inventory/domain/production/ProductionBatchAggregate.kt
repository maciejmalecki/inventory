package mm.inventory.domain.production

import kotlinx.collections.immutable.ImmutableSet
import java.math.BigDecimal

data class ProductionBatch(
    val batchNo: Int,
    val projectCode: String,
    val revision: ProjectRevision,
    val batchSize: Int
)

data class LineBooking(
    val bookingId: String,
    val itemCode: String,
    val amount: BigDecimal,
    val bookable: Boolean
)

data class ProductionBatchBooking(
    val productionRunId: String,
    val bookings: ImmutableSet<LineBooking>
) {
    val bookable = bookings.all { it.bookable }
}