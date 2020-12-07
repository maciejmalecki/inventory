package mm.inventory.domain.transactions

fun interface TransactionalHandler<T> {
    fun accept(): T
}

fun interface TransactionalConsumingHandler {
    fun accept()
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
    fun <T> execReturn(handler: TransactionalHandler<T>): T

    /**
     * Executes business code within transaction boundaries.
     * @param handler code to be executed
     */
    fun exec(handler: TransactionalConsumingHandler)
}