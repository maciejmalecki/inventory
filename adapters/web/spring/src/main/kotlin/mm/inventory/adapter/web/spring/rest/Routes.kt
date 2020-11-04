package mm.inventory.adapter.web.spring.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Routes {

    @Bean
    fun route(unitsHandler: UnitsHandler, categoriesHandler: CategoriesHandler) = coRouter {
        GET("/units", unitsHandler::allUnits)
        GET("/categories", categoriesHandler::roots)
        GET("/categories/paths", categoriesHandler::paths)
        POST("/categories", categoriesHandler::createRoot)
        POST("/categories/import", categoriesHandler::importFromText)
        GET("/categories/{categoryId}", categoriesHandler::children)
        GET("/categories/{categoryId}/path", categoriesHandler::path)
        POST("/categories/{categoryId}", categoriesHandler::createChild)
        DELETE("/categories/{categoryId}", categoriesHandler::deleteCategory)
    }
}