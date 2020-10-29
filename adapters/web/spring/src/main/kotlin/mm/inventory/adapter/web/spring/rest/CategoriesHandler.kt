package mm.inventory.adapter.web.spring.rest

import mm.inventory.adapter.web.spring.rest.dto.NewCategoryDto
import mm.inventory.app.categories.CategoryCrudRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.util.stream.Collectors

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

    suspend fun deleteCategory(req: ServerRequest): ServerResponse =
            ServerResponse
                    .ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .bodyValueAndAwait(repository.delete(req.pathVariable("categoryId").toLong()).toString())

    suspend fun path(req: ServerRequest): ServerResponse {
        val path = repository.findPathById(req.pathVariable("categoryId").toLong())
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValueAndAwait(path.stream().map {
                    it.name
                }.collect(Collectors.joining("/")))
    }

    suspend fun paths(req: ServerRequest): ServerResponse =
            ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValueAndAwait(repository.findAllPathNames("/"))
}

