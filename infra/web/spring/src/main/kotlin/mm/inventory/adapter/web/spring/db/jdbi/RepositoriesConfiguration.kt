package mm.inventory.adapter.web.spring.db.jdbi

import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiQuery
import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiSelector
import mm.inventory.adapters.store.jdbi.items.ItemJdbiQuery
import mm.inventory.adapters.store.jdbi.items.ItemJdbiRepository
import mm.inventory.adapters.store.jdbi.transactions.BusinessJdbiTransaction
import mm.inventory.adapters.store.jdbi.units.UnitOfMeasurementJdbiSelector
import mm.inventory.domain.items.behaviors.CreateItem
import mm.inventory.domain.items.behaviors.UpdateItem
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoriesConfiguration(
    private val jdbi: Jdbi
) {
    @Bean
    fun itemClassSelector() = ItemClassJdbiSelector(jdbi)

    @Bean
    fun itemClassQuery() = ItemClassJdbiQuery(jdbi)

    @Bean
    fun itemSelector() = ItemJdbiRepository(jdbi, itemClassSelector())

    @Bean
    fun unitOfMeasurementSelector() = UnitOfMeasurementJdbiSelector(jdbi)

    @Bean
    fun itemCreator() = CreateItem(businessTransaction(), itemClassSelector(), itemSelector())

    @Bean
    fun itemUpdater() = UpdateItem(businessTransaction(), itemSelector(), itemSelector(), itemClassSelector())

    @Bean
    fun itemCrudQuery() = ItemJdbiQuery(jdbi)

    @Bean
    fun businessTransaction() = BusinessJdbiTransaction(jdbi)
}