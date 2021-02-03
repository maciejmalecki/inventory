package mm.inventory.adapter.web.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InventoryWebFluxApplication

fun main(args: Array<String>) {
    runApplication<InventoryWebFluxApplication>(*args)
}
