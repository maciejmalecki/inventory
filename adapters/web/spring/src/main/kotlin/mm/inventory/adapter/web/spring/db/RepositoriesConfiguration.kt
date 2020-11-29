package mm.inventory.adapter.web.spring.db

import io.r2dbc.client.R2dbc
import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiRepository
import mm.inventory.adapters.store.jdbi.units.UnitOfMeasurementJdbiRepository
import mm.inventory.adapters.store.r2dbc.CategoryCrudR2dbcRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoriesConfiguration(val r2dbc: R2dbc, val jdbi: Jdbi) {

    @Bean
    fun categoriesRepository() = CategoryCrudR2dbcRepository(r2dbc)

    @Bean
    fun itemClassRepository() = ItemClassJdbiRepository(jdbi)

    @Bean
    fun unitOfMeasurementRepository() = UnitOfMeasurementJdbiRepository(jdbi)
}