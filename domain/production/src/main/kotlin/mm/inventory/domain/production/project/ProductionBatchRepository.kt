package mm.inventory.domain.production.project

import mm.inventory.domain.shared.NotFoundException

interface ProductionBatchRepository {
    fun find(projectCode: String, batchNo: Int): ProductionBatch?

    fun get(projectCode: String, batchNo: Int): ProductionBatch =
        find(projectCode, batchNo)
            ?: throw NotFoundException("Production batch #$batchNo for project $projectCode cannot be found.")
}