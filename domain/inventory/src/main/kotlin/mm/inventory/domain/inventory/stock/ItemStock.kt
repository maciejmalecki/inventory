package mm.inventory.domain.inventory.stock

import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.mutations.Mutable
import mm.inventory.domain.shared.mutations.MutatingCommand
import mm.inventory.domain.shared.types.ItemStockId
import java.math.BigDecimal

data class ItemStock(
    val id: ItemStockId,
    val amount: BigDecimal
) {
    fun mutable() = MutableItemStock(this)
}

class MutableItemStock(_snapshot: ItemStock) : Mutable<ItemStock>(_snapshot) {

    val itemStock: ItemStock
        get() = snapshot

    fun replenish(amount: BigDecimal): MutableItemStock {
        if (amount <= BigDecimal.ZERO) {
            throw InvalidDataException("Cannot replenish with negative value.")
        }
        append(
            ReplenishCommand(snapshot, amount),
            snapshot.copy(amount = snapshot.amount + amount)
        )
        return this
    }

    fun deduct(amount: BigDecimal): MutableItemStock {
        if (amount <= BigDecimal.ZERO) {
            throw InvalidDataException("Cannot deduct with negative value.")
        }
        append(
            DeductCommand(snapshot, amount),
            snapshot.copy(amount = snapshot.amount - amount)
        )
        return this
    }
}

data class ReplenishCommand(override val base: ItemStock, val amount: BigDecimal) : MutatingCommand<ItemStock>
data class DeductCommand(override val base: ItemStock, val amount: BigDecimal) : MutatingCommand<ItemStock>
