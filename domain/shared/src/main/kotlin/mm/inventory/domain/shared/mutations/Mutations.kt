package mm.inventory.domain.shared.mutations

import mm.inventory.domain.shared.NoChangeException
import io.vavr.collection.List as VavrList

interface MutatingCommand<T> {
    val base: T
}

typealias MutatingCommandHandler<T> = (command: MutatingCommand<T>) -> Unit

open class Mutable<T>(private var _snapshot: T) {

    var snapshot: T
        get() = _snapshot
        protected set(value) {
            _snapshot = value
        }

    val hasMutations: Boolean
        get() = !mutations.empty

    private var mutations: Mutations<T> = Mutations()

    fun handleAll(handler: MutatingCommandHandler<T>) = mutations.handleAll(handler)

    protected fun append(command: MutatingCommand<T>, next: T): Mutable<T> {
        mutations = mutations.append(command)
        snapshot = next
        return this
    }
}

data class Mutations<T>(
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
