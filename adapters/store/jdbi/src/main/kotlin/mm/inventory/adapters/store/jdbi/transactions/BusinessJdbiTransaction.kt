package mm.inventory.adapters.store.jdbi.transactions

import mm.inventory.domain.transactions.BusinessTransaction
import mm.inventory.domain.transactions.TransactionalConsumingHandler
import mm.inventory.domain.transactions.TransactionalHandler
import org.jdbi.v3.core.Jdbi

class BusinessJdbiTransaction(private val db: Jdbi) : BusinessTransaction {

    override fun <T> inTransaction(handler: TransactionalHandler<T>): T =
        db.inTransaction<T, RuntimeException> { handler.accept() }

    override fun useTransaction(handler: TransactionalConsumingHandler) =
        db.useTransaction<RuntimeException> { handler.accept() }
}