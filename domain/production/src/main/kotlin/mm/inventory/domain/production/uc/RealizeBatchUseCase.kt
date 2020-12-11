package mm.inventory.domain.production.uc

import mm.inventory.domain.inventory.ItemStockRepository
import mm.inventory.domain.production.PRODUCTION_ROLE
import mm.inventory.domain.production.PRODUCTION_WRITER_ROLE
import mm.inventory.domain.production.ProductionBatchBooking
import mm.inventory.domain.production.ProductionBatchBookingRepository
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction

/**
 * Realize production batch, that must be booked beforehand.
 */
class RealizeBatchUseCase(
    private val tx: BusinessTransaction,
    private val sec: SecurityGuard,
    private val productionBatchBookingRepository: ProductionBatchBookingRepository,
    private val itemStockRepository: ItemStockRepository
) {

    fun execute(projectCode: String, revisionCode: String, batchId: Int) =
        sec.withAllRoles(PRODUCTION_ROLE, PRODUCTION_WRITER_ROLE) {
            tx.inTransaction {
                val booking = productionBatchBookingRepository.findByBatchId(projectCode, revisionCode, batchId)
                    ?: throw BatchRealizationException("No booking for project $projectCode rev. $revisionCode #$batchId found.")
                if (!booking.bookable) {
                    throw BatchRealizationException("Not enough stock to realize booking $projectCode rev. $revisionCode #$batchId.")
                }
                realizeBooking(booking)
            }
        }

    private fun realizeBooking(booking: ProductionBatchBooking) {
        booking.bookings.forEach { bookingCapability ->
            val deductedAmount = itemStockRepository.deduct(bookingCapability.booking)
            if (deductedAmount < bookingCapability.booking.amount) {
                throw BatchRealizationException("Booking not possible due to insufficient stock for ${bookingCapability.booking.itemCode} ($deductedAmount < ${bookingCapability.booking.amount}).")
            }
        }
    }
}