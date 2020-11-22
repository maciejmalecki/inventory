package mm.inventory.domain.production

interface ProductionPlanReservationRepository {
    fun store(productionPlanReservation: ProductionPlanReservation): ProductionPlanReservation
}