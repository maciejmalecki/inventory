package mm.inventory.domain.production

import kotlinx.collections.immutable.ImmutableSet
import mm.inventory.domain.inventory.Reservation
import mm.inventory.domain.items.Item
import java.math.BigDecimal

data class ProductionPlanItem(
        val item: Item,
        val amount: BigDecimal
)

class ProductionPlanReservation(
        private val itemReservations: ImmutableSet<ProductionPlanItemReservation>
) {
    val canBeRealized: Boolean
        get() = itemReservations.stream().allMatch {
            it.reservation.bookedAmount == it.reservation.reservedAmount
        }
}

data class ProductionPlanItemReservation(
        val productionPlanItem: ProductionPlanItem,
        val reservation: Reservation
)

data class ProductionPlan(
        val items: ImmutableSet<ProductionPlanItem>
)