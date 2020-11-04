package mm.inventory.adapter.web.spring.db

import mm.inventory.app.categories.CategoryCrudRepository
import mm.inventory.app.categories.import.CategoryImporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ImporterConfiguration(val repository: CategoryCrudRepository) {

    @Bean
    fun importer() = CategoryImporter(repository)
}