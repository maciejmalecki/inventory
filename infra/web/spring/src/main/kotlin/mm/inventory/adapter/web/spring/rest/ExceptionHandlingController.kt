package mm.inventory.adapter.web.spring.rest

import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController

@RestController
class ExceptionHandlingController {

    @ExceptionHandler(NotFoundException::class)
    fun notFound(exception: NotFoundException): ResponseEntity<Any> =
        ResponseEntity.notFound().header("message", exception.message).build()

    @ExceptionHandler(InvalidDataException::class)
    fun invalidData(exception: InvalidDataException): ResponseEntity<Any> =
        ResponseEntity.badRequest().header("message", exception.message).build()
}