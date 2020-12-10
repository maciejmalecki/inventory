package mm.inventory.app.productionfacade

import mm.inventory.domain.production.project.BookBatch
import mm.inventory.domain.production.project.ProductionBatchBookingRepository
import mm.inventory.domain.production.project.ProductionBatchRepository
import mm.inventory.domain.production.project.RealizeBatch

class ProductionFacade(
    private val productionBatchRepository: ProductionBatchRepository,
    private val productionBatchBookingRepository: ProductionBatchBookingRepository,
    private val bookBatch: BookBatch,
    private val realizeBatch: RealizeBatch
) {
    fun findProductionBatch(projectCode: String, batchNo: Int) = productionBatchRepository.find(projectCode, batchNo);

    fun bookBatch(projectCode: String, batchNo: Int) = bookBatch.execute(projectCode, batchNo)

    fun findProductionBatchBooking(projectCode: String, revisionCode: String, batchId: Int) =
        productionBatchBookingRepository.findByBatchId(projectCode, revisionCode, batchId)

    fun realizeBatch(projectCode: String, revisionCode: String, batchId: Int) =
        realizeBatch.execute(projectCode, revisionCode, batchId)
}