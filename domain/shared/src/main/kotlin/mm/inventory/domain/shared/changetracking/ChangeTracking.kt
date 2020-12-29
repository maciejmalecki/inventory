package mm.inventory.domain.shared.changetracking

import mm.inventory.domain.shared.NoChangeException
import io.vavr.collection.List as VavrList

interface MutatingCommand<T> {
    val base: T
    fun mutate(): T
}

typealias MutatingCommandHandler<T> = (command: MutatingCommand<T>) -> T

data class Mutations<T>(
    private val commands: VavrList<MutatingCommand<T>> = VavrList.empty()
) {

    fun append(command: MutatingCommand<T>): Mutations<T> = Mutations(commands.append(command))

    fun handleAll(handler: MutatingCommandHandler<T>): T = handleAll(handler, commands)

    private tailrec fun handleAll(
        handler: MutatingCommandHandler<T>,
        tailCommands: VavrList<MutatingCommand<T>>
    ): T =
        when (tailCommands.size()) {
            0 -> throw NoChangeException("No change detected for ${this.javaClass.genericSuperclass.typeName}.")
            1 -> handler.invoke(tailCommands.first())
            else -> handleAll(handler, tailCommands.subSequence(1))
        }
}
