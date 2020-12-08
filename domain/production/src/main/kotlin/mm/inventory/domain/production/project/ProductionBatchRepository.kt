package mm.inventory.domain.production.project

interface ProductionBatchRepository {
    fun find(projectCode: String, batchNo: Int): ProductionBatch?

    fun get(projectCode: String, batchNo: Int): ProductionBatch =
        find(projectCode, batchNo) ?: throw RuntimeException("Production batch #$batchNo for project $projectCode")
}