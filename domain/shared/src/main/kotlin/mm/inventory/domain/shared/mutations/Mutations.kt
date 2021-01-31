package mm.inventory.domain.shared.mutations

import mm.inventory.domain.shared.NoChangeException
import io.vavr.collection.List as VavrList

interface MutatingCommand<T> {
    /**
     * State of the entity before mutation applied.
     */
    val base: T
}

typealias MutatingCommandHandler<T> = (command: MutatingCommand<T>) -> Unit

typealias MutatingCommandHandlerWithResponse<T, R> = (command: MutatingCommand<T>, previousResponse: R?) -> R

/**
 * Mutable wrapper for an entity/an aggregate. This wrapper is capable of registering changes as a list of commands.
 */
open class Mutable<T>(private var _snapshot: T) {

    /**
     * Current state of the entity.
     */
    var snapshot: T
        get() = _snapshot
        protected set(value) {
            _snapshot = value
        }

    /**
     * Are there any mutations available.
     */
    val hasMutations: Boolean
        get() = !mutations.empty

    private var mutations: Mutations<T> = Mutations()

    /**
     * "Consume" all changes by performing handler on each of them. Changes are empty afterwards.
     */
    fun consume(handler: MutatingCommandHandler<T>) {
        consume { command: MutatingCommand<T>, _: Any? ->
            handler.invoke(command)
            null
        }
    }

    fun <R> consume(handler: MutatingCommandHandlerWithResponse<T, R>): R? {
        val result = mutations.handleAll(handler, null)
        mutations = Mutations()
        return result
    }

    /**
     * Append new change.
     * @param command encapsulating the state change
     * @param next calculated next state of the entity
     */
    protected fun append(command: MutatingCommand<T>, next: T) {
        mutations = mutations.append(command)
        snapshot = next
    }
}

internal class Mutations<T>(
    private val commands: VavrList<MutatingCommand<T>> = VavrList.empty()
) {

    val empty = commands.isEmpty

    fun append(command: MutatingCommand<T>): Mutations<T> = Mutations(commands.append(command))

    fun <R> handleAll(handler: MutatingCommandHandlerWithResponse<T, R>, previousResponse: R?) =
        handleAll(handler, previousResponse, commands)

    private tailrec fun <R> handleAll(
        handler: MutatingCommandHandlerWithResponse<T, R>,
        previousResponse: R?,
        tailCommands: VavrList<MutatingCommand<T>>
    ): R? =
        when (tailCommands.size()) {
            0 -> throw NoChangeException("No change detected for ${this.javaClass.genericSuperclass.typeName}.")
            1 -> handler.invoke(tailCommands.first(), previousResponse)
            else -> {
                val response = handler.invoke(tailCommands.first(), previousResponse)
                handleAll(handler, response, tailCommands.subSequence(1))
            }
        }
}
