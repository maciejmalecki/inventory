package mm.inventory.adapter.web.spring.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Routes {

    @Bean
    fun route(unitsHandler: UnitsHandler, categoriesHandler: CategoriesHandler) = coRouter {
        GET("/units", unitsHandler::allUnits)
        GET("/categories", categoriesHandler::roots)
        POST("/categories", categoriesHandler::createRoot)
    }
}