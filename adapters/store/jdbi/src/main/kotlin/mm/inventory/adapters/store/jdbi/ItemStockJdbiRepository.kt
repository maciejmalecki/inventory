package mm.inventory.adapters.store.jdbi

import mm.inventory.domain.inventory.ItemStockRepository
import mm.inventory.domain.inventory.Reservation
import org.jdbi.v3.core.Jdbi
import java.math.BigDecimal


class ItemStockJdbiRepository(val jndi: Jdbi) : ItemStockRepository {
    override fun replenish(itemCode: String, amount: BigDecimal) {
        TODO("Not yet implemented")
    }

    override fun reserve(itemCode: String, amount: BigDecimal): Reservation {
        TODO("Not yet implemented")
    }

    override fun cancel(reservation: Reservation) {
        TODO("Not yet implemented")
    }

    override fun deduct(reservation: Reservation) {
        TODO("Not yet implemented")
    }
}