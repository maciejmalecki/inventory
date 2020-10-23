package mm.inventory.adapter.web.spring

import io.r2dbc.client.R2dbc
import mm.inventory.adapters.store.sql.UnitRepositorySqlImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Repositories(val r2dbc: R2dbc) {

    @Bean
    fun unitsRepository() = UnitRepositorySqlImpl(r2dbc)
}