package mm.inventory.domain.itemclasses

import reactor.core.publisher.Flux

interface UnitOfMeasurementRepository {
    suspend fun findAll(): Flux<UnitOfMeasurement>
}