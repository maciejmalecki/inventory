package mm.inventory.adapter.web.spring

import mm.inventory.adapters.store.sql.UnitRepositorySqlImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Repositories {

    @Bean
    fun unitsRepository() = UnitRepositorySqlImpl()
}