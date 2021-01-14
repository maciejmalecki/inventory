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
        mutations.handleAll(handler)
        mutations = Mutations()
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

    fun handleAll(handler: MutatingCommandHandler<T>) = handleAll(handler, commands)

    private tailrec fun handleAll(
        handler: MutatingCommandHandler<T>,
        tailCommands: VavrList<MutatingCommand<T>>
    ): Unit =
        when (tailCommands.size()) {
            0 -> throw NoChangeException("No change detected for ${this.javaClass.genericSuperclass.typeName}.")
            1 -> handler.invoke(tailCommands.first())
            else -> {
                handler.invoke(tailCommands.first())
                handleAll(handler, tailCommands.subSequence(1))
            }
        }
}
