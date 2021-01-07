package mm.inventory.adapter.web.spring.db.jdbi

import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiQuery
import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiRepository
import mm.inventory.adapters.store.jdbi.items.ItemJdbiQuery
import mm.inventory.adapters.store.jdbi.items.ItemJdbiRepository
import mm.inventory.adapters.store.jdbi.transactions.BusinessJdbiTransaction
import mm.inventory.adapters.store.jdbi.units.UnitOfMeasurementJdbiRepository
import mm.inventory.domain.items.item.ItemFactory
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoriesConfiguration(
    private val jdbi: Jdbi
) {
    @Bean
    fun itemClassSelector() = ItemClassJdbiRepository(jdbi)

    @Bean
    fun itemClassQuery() = ItemClassJdbiQuery(jdbi)

    @Bean
    fun itemSelector() = ItemJdbiRepository(jdbi, itemClassSelector())

    @Bean
    fun unitOfMeasurementSelector() = UnitOfMeasurementJdbiRepository(jdbi)

    @Bean
    fun itemCreator() = ItemFactory(businessTransaction(), itemClassSelector(), itemSelector())

    @Bean
    fun itemCrudQuery() = ItemJdbiQuery(jdbi)

    @Bean
    fun businessTransaction() = BusinessJdbiTransaction(jdbi)
}