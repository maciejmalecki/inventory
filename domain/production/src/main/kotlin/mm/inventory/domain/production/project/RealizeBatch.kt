package mm.inventory.domain.production.project

import mm.inventory.domain.shared.transactions.BusinessTransaction

class RealizeBatch(private val tx: BusinessTransaction) {

    fun execute() = tx.inTransaction {

    }
}