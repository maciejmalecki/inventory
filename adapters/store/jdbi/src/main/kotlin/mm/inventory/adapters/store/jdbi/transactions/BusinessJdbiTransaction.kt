package mm.inventory.adapters.store.jdbi.transactions

import mm.inventory.domain.transactions.BusinessTransaction
import mm.inventory.domain.transactions.TransactionalConsumingHandler
import mm.inventory.domain.transactions.TransactionalHandler
import org.jdbi.v3.core.Jdbi

class BusinessJdbiTransaction(private val db: Jdbi) : BusinessTransaction {

    override fun <T> execReturn(handler: TransactionalHandler<T>): T =
        db.inTransaction<T, RuntimeException> { handler.accept() }

    override fun exec(handler: TransactionalConsumingHandler) =
        db.useTransaction<RuntimeException> { handler.accept() }
}