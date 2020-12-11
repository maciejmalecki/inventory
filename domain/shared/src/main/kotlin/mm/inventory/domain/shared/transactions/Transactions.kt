package mm.inventory.domain.shared.transactions

fun interface TransactionalHandler<T> {
    fun accept(): T
}

/**
 * Allows to define transaction bracketing.
 */
interface BusinessTransaction {
    /**
     * Executes business code within transaction boundaries and returns data of type T.
     * @param handler code to be executed
     * @param T returned payload data type
     */
    fun <T> inTransaction(handler: TransactionalHandler<T>): T
}