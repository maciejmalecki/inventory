package mm.inventory.adapter.web.spring.db

import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiRepository
import mm.inventory.adapters.store.jdbi.items.ItemCrudJdbiRepository
import mm.inventory.adapters.store.jdbi.items.ItemJdbiRepository
import mm.inventory.adapters.store.jdbi.transactions.BusinessJdbiTransaction
import mm.inventory.adapters.store.jdbi.units.UnitOfMeasurementJdbiRepository
import mm.inventory.app.itemsfacade.item.ItemFacade
import mm.inventory.app.itemsfacade.itemclass.ItemClassFacade
import mm.inventory.domain.items.CreateItem
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoriesConfiguration(private val jdbi: Jdbi) {

    @Bean
    fun itemClassRepository() = ItemClassJdbiRepository(jdbi)

    @Bean
    fun itemRepository() = ItemJdbiRepository(jdbi, itemClassRepository())

    @Bean
    fun unitOfMeasurementRepository() = UnitOfMeasurementJdbiRepository(jdbi)

    @Bean
    fun itemCreator() = CreateItem(businessTransaction(), itemClassRepository(), itemRepository())

    @Bean
    fun itemCrudRepository() = ItemCrudJdbiRepository(jdbi)

    @Bean
    fun businessTransaction() = BusinessJdbiTransaction(jdbi)

    @Bean
    fun itemClassFacade() = ItemClassFacade(itemClassRepository())

    @Bean
    fun itemFacade() = ItemFacade(itemRepository(), itemCrudRepository(), itemCreator())
}