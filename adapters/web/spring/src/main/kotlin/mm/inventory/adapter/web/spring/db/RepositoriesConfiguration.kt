package mm.inventory.adapter.web.spring.db

import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiRepository
import mm.inventory.adapters.store.jdbi.units.UnitOfMeasurementJdbiRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoriesConfiguration(val jdbi: Jdbi) {

    @Bean
    fun itemClassRepository() = ItemClassJdbiRepository(jdbi)

    @Bean
    fun unitOfMeasurementRepository() = UnitOfMeasurementJdbiRepository(jdbi)
}