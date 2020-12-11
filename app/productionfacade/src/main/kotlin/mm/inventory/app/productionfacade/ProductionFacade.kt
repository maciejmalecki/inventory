package mm.inventory.app.productionfacade

import mm.inventory.domain.production.behaviors.BookBatch
import mm.inventory.domain.production.ProductionBatchBookingSelector
import mm.inventory.domain.production.ProductionBatchSelector
import mm.inventory.domain.production.behaviors.RealizeBatch

class ProductionFacade(
    private val productionBatchSelector: ProductionBatchSelector,
    private val productionBatchBookingSelector: ProductionBatchBookingSelector,
    private val bookBatch: BookBatch,
    private val realizeBatch: RealizeBatch
) {
    fun findProductionBatch(projectCode: String, batchNo: Int) = productionBatchSelector.find(projectCode, batchNo);

    fun bookBatch(projectCode: String, batchNo: Int) = bookBatch.execute(projectCode, batchNo)

    fun findProductionBatchBooking(projectCode: String, revisionCode: String, batchId: Int) =
        productionBatchBookingSelector.findByBatchId(projectCode, revisionCode, batchId)

    fun realizeBatch(projectCode: String, revisionCode: String, batchId: Int) =
        realizeBatch.execute(projectCode, revisionCode, batchId)
}