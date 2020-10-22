package mm.inventory.adapter.web.spring

import mm.inventory.domain.itemclasses.UnitRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@RestController
class UnitsHandler(val unitRepository: UnitRepository) {
    suspend fun allUnits(req: ServerRequest) = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(unitRepository.findAll())
}