package mm.inventory.adapter.web.spring.rest

import mm.inventory.adapter.web.spring.rest.dto.NewCategoryDto
import mm.inventory.app.categories.CategoryCrudRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@RestController
class CategoriesHandler(val repository: CategoryCrudRepository) {

    suspend fun roots(req: ServerRequest): ServerResponse =
            ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValueAndAwait(repository.findAllRoot())

    suspend fun children(req: ServerRequest): ServerResponse =
            ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValueAndAwait(repository.findAll(req.pathVariable("categoryId").toLong()))

    suspend fun createRoot(req: ServerRequest): ServerResponse {
        val newCategoryDto: NewCategoryDto = req.awaitBody()
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait(repository.create(newCategoryDto.code, newCategoryDto.name))
    }

    suspend fun createChild(req: ServerRequest): ServerResponse {
        val newCategoryDto: NewCategoryDto = req.awaitBody()
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait(repository.create(newCategoryDto.code, newCategoryDto.name, req.pathVariable("categoryId").toLong()))
    }
}

