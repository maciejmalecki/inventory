package mm.inventory.app.productionfacade

import mm.inventory.domain.production.uc.BookBatchUseCase
import mm.inventory.domain.production.ProductionBatchBookingRepository
import mm.inventory.domain.production.ProductionBatchRepository
import mm.inventory.domain.production.uc.RealizeBatchUseCase

class ProductionFacade(
    private val productionBatchRepository: ProductionBatchRepository,
    private val productionBatchBookingRepository: ProductionBatchBookingRepository,
    private val bookBatchUseCase: BookBatchUseCase,
    private val realizeBatchUseCase: RealizeBatchUseCase
) {
    fun findProductionBatch(projectCode: String, batchNo: Int) = productionBatchRepository.find(projectCode, batchNo);

    fun bookBatch(projectCode: String, batchNo: Int) = bookBatchUseCase.execute(projectCode, batchNo)

    fun findProductionBatchBooking(projectCode: String, revisionCode: String, batchId: Int) =
        productionBatchBookingRepository.findByBatchId(projectCode, revisionCode, batchId)

    fun realizeBatch(projectCode: String, revisionCode: String, batchId: Int) =
        realizeBatchUseCase.execute(projectCode, revisionCode, batchId)
}