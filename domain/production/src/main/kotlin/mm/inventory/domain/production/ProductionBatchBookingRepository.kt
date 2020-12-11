package mm.inventory.domain.production

import mm.inventory.domain.shared.NotFoundException

interface ProductionBatchBookingRepository {
    fun findById(productionRunId: String): ProductionBatchBooking?

    fun get(productionRunId: String): ProductionBatchBooking = findById(productionRunId)
        ?: throw NotFoundException("Production batch booking $productionRunId cannot be found.")

    fun findByBatchId(projectCode: String, revisionCode: String, batchId: Int): ProductionBatchBooking?

    fun get(projectCode: String, revisionCode: String, batchId: Int): ProductionBatchBooking =
        findByBatchId(projectCode, revisionCode, batchId)
            ?: throw NotFoundException("Production batch booking for project $projectCode, rev. $revisionCode and batch id $batchId cannot be found.")
}
