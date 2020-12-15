package mm.inventory.domain.production.behaviors

import mm.inventory.domain.production.BookingRepository
import mm.inventory.domain.production.PRODUCTION_ROLE
import mm.inventory.domain.production.PRODUCTION_WRITER_ROLE
import mm.inventory.domain.production.ProductionBatchBooking
import mm.inventory.domain.production.ProductionBatchBookingSelector
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction

/**
 * Realize production batch, that must be booked beforehand.
 */
class RealizeBatch(
    private val tx: BusinessTransaction,
    private val sec: SecurityGuard,
    private val productionBatchBookingSelector: ProductionBatchBookingSelector,
    private val bookingRepository: BookingRepository
) {

    fun execute(projectCode: String, revisionCode: String, batchId: Int) =
        sec.requireAllRoles(PRODUCTION_ROLE, PRODUCTION_WRITER_ROLE) {
            tx.inTransaction {
                val booking = productionBatchBookingSelector.findByBatchId(projectCode, revisionCode, batchId)
                    ?: throw BatchRealizationException("No booking for project $projectCode rev. $revisionCode #$batchId found.")
                if (!booking.bookable) {
                    throw BatchRealizationException("Not enough stock to realize booking $projectCode rev. $revisionCode #$batchId.")
                }
                realizeBooking(booking)
            }
        }

    private fun realizeBooking(booking: ProductionBatchBooking) {
        booking.bookings.forEach { bookingCapability ->
            val deductedAmount = bookingRepository.deduct(bookingCapability)
            if (deductedAmount < bookingCapability.amount) {
                throw BatchRealizationException("Booking not possible due to insufficient stock for ${bookingCapability.itemCode} ($deductedAmount < ${bookingCapability.amount}).")
            }
        }
    }
}