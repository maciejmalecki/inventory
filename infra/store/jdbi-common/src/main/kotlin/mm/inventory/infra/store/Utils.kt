package mm.inventory.infra.store

import mm.inventory.domain.shared.InvalidDataException

fun updateAndExpect(expectedCount: Int, handler: () -> Int) {
    val ctr = handler.invoke()
    if (ctr != expectedCount) {
        throw InvalidDataException("Wrong update count; expected $expectedCount but got $ctr.")
    }
}
