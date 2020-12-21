package mm.inventory.adapter.web.spring.db.jdbi

import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiQuery
import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiSelector
import mm.inventory.adapters.store.jdbi.items.ItemJdbiQuery
import mm.inventory.adapters.store.jdbi.items.ItemJdbiMutator
import mm.inventory.adapters.store.jdbi.items.ItemJdbiSelector
import mm.inventory.adapters.store.jdbi.transactions.BusinessJdbiTransaction
import mm.inventory.adapters.store.jdbi.units.UnitOfMeasurementJdbiSelector
import mm.inventory.domain.items.behaviors.CreateItem
import mm.inventory.domain.items.behaviors.UpdateItem
import mm.inventory.domain.shared.security.SecurityGuard
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoriesConfiguration(
    private val jdbi: Jdbi,
    private val securityGuard: SecurityGuard
) {
    @Bean
    fun itemClassSelector() = ItemClassJdbiSelector(jdbi)

    @Bean
    fun itemClassQuery() = ItemClassJdbiQuery(jdbi)

    @Bean
    fun itemSelector() = ItemJdbiSelector(jdbi, itemClassSelector())

    @Bean
    fun itemMutator() = ItemJdbiMutator(jdbi)

    @Bean
    fun unitOfMeasurementSelector() = UnitOfMeasurementJdbiSelector(jdbi)

    @Bean
    fun itemCreator() = CreateItem(businessTransaction(), securityGuard, itemClassSelector(), itemMutator())

    @Bean
    fun itemUpdater() = UpdateItem(businessTransaction(), securityGuard, itemSelector(), itemMutator(), itemClassSelector())

    @Bean
    fun itemCrudQuery() = ItemJdbiQuery(jdbi)

    @Bean
    fun businessTransaction() = BusinessJdbiTransaction(jdbi)
}