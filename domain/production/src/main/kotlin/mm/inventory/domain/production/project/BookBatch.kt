package mm.inventory.domain.production.project

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.inventory.ItemStockRepository
import mm.inventory.domain.transactions.BusinessTransaction

class BookBatch(
    private val tx: BusinessTransaction,
    private val productionBatchRepository: ProductionBatchRepository,
    private val itemStockRepository: ItemStockRepository
) {

    fun book(projectCode: String, batchNo: Int): ProductionBatchBooking = tx.execReturn {
        val productionBatch = productionBatchRepository.get(projectCode, batchNo)
        val productionRunId =
            "${productionBatch.projectCode}/${productionBatch.revision.revisionCode}#${productionBatch.batchNo}"

        val bookings = productionBatch.revision.itemUsages.map { usage ->
            book(productionRunId, usage, productionBatch.batchSize)
        }
        ProductionBatchBooking(productionRunId, bookings.toImmutableSet())
    }

    private fun book(productionRunId: String, usage: Usage, batchSize: Int) =
        itemStockRepository.book(productionRunId, usage.itemCode, usage.amount * batchSize.toBigDecimal())
}
