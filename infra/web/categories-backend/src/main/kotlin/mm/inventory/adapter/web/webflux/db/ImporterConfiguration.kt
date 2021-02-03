package mm.inventory.adapter.web.webflux.db

import mm.inventory.app.categories.CategoryCrudRepository
import mm.inventory.app.categories.CategoryImporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ImporterConfiguration(val repository: CategoryCrudRepository) {

    @Bean
    fun importer() = CategoryImporter(repository)
}