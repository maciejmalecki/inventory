package mm.inventory.adapter.web.spring.rest

import mm.inventory.domain.itemclasses.UnitOfMeasurementRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@RestController
class UnitsHandler(val unitOfMeasurementRepository: UnitOfMeasurementRepository) {
    suspend fun allUnits(req: ServerRequest): ServerResponse =
            ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValueAndAwait(unitOfMeasurementRepository.findAll())
}