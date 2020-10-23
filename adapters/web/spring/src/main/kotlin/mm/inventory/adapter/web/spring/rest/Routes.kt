package mm.inventory.adapter.web.spring.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Routes {

    @Bean
    fun route(unitsHandler: UnitsHandler) = coRouter {
        GET("/units", unitsHandler::allUnits)
    }
}