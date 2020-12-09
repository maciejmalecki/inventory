package mm.inventory.domain.production.project

import mm.inventory.domain.inventory.ItemStockRepository
import mm.inventory.domain.shared.transactions.BusinessTransaction

class RealizationException(msg: String) : RuntimeException(msg)

class RealizeBatch(
    private val tx: BusinessTransaction,
    private val productionBatchBookingRepository: ProductionBatchBookingRepository,
    private val itemStockRepository: ItemStockRepository
) {

    fun execute(projectCode: String, revisionCode: String, batchId: Int) = tx.inTransaction {
        val booking = productionBatchBookingRepository.findByBatchId(projectCode, revisionCode, batchId)
            ?: throw RealizationException("No booking for project $projectCode rev. $revisionCode #$batchId found.")

        if (!booking.bookable) {
            throw RealizationException("Not enough stock to realize booking $projectCode rev. $revisionCode #$batchId.")
        }

        booking.bookings.forEach { bookingCapability ->
            itemStockRepository.deduct(bookingCapability.booking)
        }
    }
}