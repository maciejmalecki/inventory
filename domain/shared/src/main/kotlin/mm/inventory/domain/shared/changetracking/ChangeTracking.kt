package mm.inventory.domain.shared.changetracking

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

    fun handleAll(base: T, handler: MutatingCommandHandler<T>): T = handleAll(base, handler, commands)

    private tailrec fun handleAll(
        base: T,
        handler: MutatingCommandHandler<T>,
        tailCommands: VavrList<MutatingCommand<T>>
    ): T =
        when (tailCommands.size()) {
            0 -> base
            1 -> handler.invoke(tailCommands.first())
            else -> handleAll(base, handler, tailCommands.subSequence(1))
        }
}
