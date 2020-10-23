package mm.inventory.adapter.web.spring.db

import io.r2dbc.client.R2dbc
import mm.inventory.adapters.store.sql.UnitOfMeasurementSqlRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoriesConfiguration(val r2dbc: R2dbc) {

    @Bean
    fun unitsRepository() = UnitOfMeasurementSqlRepository(r2dbc)
}