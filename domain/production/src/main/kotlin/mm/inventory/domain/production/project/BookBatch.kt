package mm.inventory.domain.production.project

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.inventory.ItemStockRepository
import mm.inventory.domain.production.PRODUCTION_ROLE
import mm.inventory.domain.production.PRODUCTION_WRITER_ROLE
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction

/**
 * Books item stock for production batch.
 */
class BookBatch(
    private val tx: BusinessTransaction,
    private val sec: SecurityGuard,
    private val productionBatchRepository: ProductionBatchRepository,
    private val itemStockRepository: ItemStockRepository
) {

    /**
     * Performs booking.
     * @param projectCode code of the project
     * @param batchNo batch serial number
     */
    fun execute(projectCode: String, batchNo: Int): ProductionBatchBooking =
        sec.withAllRoles(PRODUCTION_ROLE, PRODUCTION_WRITER_ROLE) {
            tx.inTransaction {
                val productionBatch = productionBatchRepository.get(projectCode, batchNo)
                val productionRunId =
                    "${productionBatch.projectCode}/${productionBatch.revision.revisionCode}#${productionBatch.batchNo}"

                val bookings = productionBatch.revision.itemUsages.map { usage ->
                    book(productionRunId, usage, productionBatch.batchSize)
                }
                ProductionBatchBooking(productionRunId, bookings.toImmutableSet())
            }
        }

    private fun book(productionRunId: String, usage: Usage, batchSize: Int) =
        itemStockRepository.book(productionRunId, usage.itemCode, usage.amount * batchSize.toBigDecimal())
}
