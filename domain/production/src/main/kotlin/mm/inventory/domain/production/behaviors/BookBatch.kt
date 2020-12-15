package mm.inventory.domain.production.behaviors

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.production.BookingRepository
import mm.inventory.domain.production.PRODUCTION_ROLE
import mm.inventory.domain.production.PRODUCTION_WRITER_ROLE
import mm.inventory.domain.production.ProductionBatchBooking
import mm.inventory.domain.production.ProductionBatchSelector
import mm.inventory.domain.production.Usage
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction

/**
 * Books item stock for production batch.
 */
class BookBatch(
    private val tx: BusinessTransaction,
    private val sec: SecurityGuard,
    private val productionBatchSelector: ProductionBatchSelector,
    private val bookingRepository: BookingRepository
) {

    /**
     * Performs booking.
     * @param projectCode code of the project
     * @param batchNo batch serial number
     */
    fun execute(projectCode: String, batchNo: Int): ProductionBatchBooking =
        sec.requireAllRoles(PRODUCTION_ROLE, PRODUCTION_WRITER_ROLE) {
            tx.inTransaction {
                val productionBatch = productionBatchSelector.get(projectCode, batchNo)
                val productionRunId =
                    "${productionBatch.projectCode}/${productionBatch.revision.revisionCode}#${productionBatch.batchNo}"

                val bookings = productionBatch.revision.itemUsages.map { usage ->
                    book(productionRunId, usage, productionBatch.batchSize)
                }
                ProductionBatchBooking(productionRunId, bookings.toImmutableSet())
            }
        }

    private fun book(productionRunId: String, usage: Usage, batchSize: Int) =
        bookingRepository.bookLine(productionRunId, usage.itemCode, usage.amount * batchSize.toBigDecimal())
}
