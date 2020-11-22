package mm.inventory.domain.production

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.inventory.ItemStockRepository

class PlanAllocator(private val itemStockRepository: ItemStockRepository, private val productionPlanReservationRepository: ProductionPlanReservationRepository) {

    fun reserve(plan: ProductionPlan): ProductionPlanReservation {
        val reservation = ProductionPlanReservation(plan.items.map { item ->
            ProductionPlanItemReservation(item, this.itemStockRepository.reserve(item.item.code, item.amount))
        }.toImmutableSet())
        return this.productionPlanReservationRepository.store(reservation)
    }
}