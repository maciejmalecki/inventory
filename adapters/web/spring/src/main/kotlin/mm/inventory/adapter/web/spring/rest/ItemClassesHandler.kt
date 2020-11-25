package mm.inventory.adapter.web.spring.rest

import mm.inventory.domain.itemclasses.ItemClassRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@RestController
class ItemClassesHandler(private val itemClassRepository: ItemClassRepository) {

    suspend fun itemClass(req: ServerRequest): ServerResponse =
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValueAndAwait(itemClassRepository.findByName(req.pathVariable("itemClassName")))
}