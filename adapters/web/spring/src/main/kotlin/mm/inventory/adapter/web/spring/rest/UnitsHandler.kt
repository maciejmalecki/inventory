package mm.inventory.adapter.web.spring.rest

import mm.inventory.adapters.store.jdbi.units.UnitRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@RestController
class UnitsHandler(val unitOfMeasurementRepository: UnitRepository) {
    suspend fun allUnits(req: ServerRequest): ServerResponse =
            ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValueAndAwait(unitOfMeasurementRepository.getAllUnits())
}