package mm.inventory.adapter.web.spring

import kotlinx.coroutines.reactive.awaitFirst
import mm.inventory.domain.itemclasses.UnitOfMeasurement
import mm.inventory.domain.itemclasses.UnitOfMeasurementRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@RestController
class UnitsHandler(val unitOfMeasurementRepository: UnitOfMeasurementRepository) {
    suspend fun allUnits(req: ServerRequest) =
            ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(unitOfMeasurementRepository.findAll(), UnitOfMeasurement::class.java)
                    .awaitFirst()
}