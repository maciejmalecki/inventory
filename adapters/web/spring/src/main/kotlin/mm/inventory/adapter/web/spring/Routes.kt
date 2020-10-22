package mm.inventory.adapter.web.spring

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Routes {

    fun route(unitsHandler: UnitsHandler) = coRouter {
        GET("/units", unitsHandler::allUnits)
    }
}