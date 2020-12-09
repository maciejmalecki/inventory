package mm.inventory.domain.production.project

import kotlinx.collections.immutable.ImmutableSet
import mm.inventory.domain.inventory.BookingCapability
import java.math.BigDecimal
import java.time.LocalDateTime

data class Project(
    val projectCode: String,
    val description: String,
    val revisions: ImmutableSet<ProjectRevision>
)

data class ProjectRevision(
    val revisionCode: String,
    val createdAt: LocalDateTime,
    val itemUsages: ImmutableSet<Usage>
)

data class Usage(
    val itemCode: String,
    val amount: BigDecimal
)

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
