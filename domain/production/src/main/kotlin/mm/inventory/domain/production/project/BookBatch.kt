package mm.inventory.domain.production.project

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.inventory.ItemStockRepository

class BookBatch(
    private val productionBatchRepository: ProductionBatchRepository,
    private val itemStockRepository: ItemStockRepository
) {

    fun book(projectCode: String, batchNo: Int): ProductionBatchBooking {
        val productionBatch = productionBatchRepository.get(projectCode, batchNo)
        val productionRunId =
            "${productionBatch.projectCode}/${productionBatch.revision.revisionCode}#${productionBatch.batchNo}"

        val bookings = productionBatch.revision.itemUsages.map { usage ->
            book(productionRunId, usage, productionBatch.batchSize)
        }
        return ProductionBatchBooking(productionRunId, bookings.toImmutableSet())
    }

    private fun book(productionRunId: String, usage: Usage, batchSize: Int) =
        itemStockRepository.book(productionRunId, usage.itemCode, usage.amount * batchSize.toBigDecimal())
}
