package mm.inventory.domain.production

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.inventory.ItemStockRepository

/**
 * Use case: allocate stock for given production plan instance.
 */
class PlanAllocator(private val itemStockRepository: ItemStockRepository, private val productionPlanReservationRepository: ProductionPlanReservationRepository) {

    /**
     * Make reservation for production plan.
     * @param plan plan instance
     */
    fun reserve(plan: ProductionPlan): ProductionPlanReservation {
        val reservation = ProductionPlanReservation(plan.items.map { item ->
            ProductionPlanItemReservation(item, this.itemStockRepository.reserve(item.item.code, item.amount))
        }.toImmutableSet())
        return this.productionPlanReservationRepository.store(reservation)
    }

    /**
     * Abandons reservation and frees all reserved resources.
     * @param planReservation
     */
    fun abandon(planReservation: ProductionPlanReservation) {
        planReservation.itemReservations.forEach {
            this.itemStockRepository.cancel(it.reservation)
        }
    }

    /**
     * Realizes production plan and effectively deducts all reserved resources.
     * @param planReservation
     */
    fun deduct(planReservation: ProductionPlanReservation) {
        if (planReservation.canBeRealized) {
            planReservation.itemReservations.forEach {
                this.itemStockRepository.deduct(it.reservation)
            }
        } else {
            throw IllegalArgumentException("Reservation incomplete for production plan")
        }
    }
}