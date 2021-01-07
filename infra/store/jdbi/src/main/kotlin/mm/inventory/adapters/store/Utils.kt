package mm.inventory.adapters.store

import mm.inventory.domain.shared.InvalidDataException

internal fun updateAndExpect(expectedCount: Int, handler: () -> Int) {
    val ctr = handler.invoke()
    if (ctr != expectedCount) {
        throw InvalidDataException("Wrong update count; expected $expectedCount but got $ctr.")
    }
}
