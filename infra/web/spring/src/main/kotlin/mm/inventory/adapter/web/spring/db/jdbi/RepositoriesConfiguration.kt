package mm.inventory.adapter.web.spring.db.jdbi

import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiRepository
import mm.inventory.adapters.store.jdbi.items.ItemCrudJdbiRepository
import mm.inventory.adapters.store.jdbi.items.ItemJdbiMutator
import mm.inventory.adapters.store.jdbi.items.ItemJdbiRepository
import mm.inventory.adapters.store.jdbi.transactions.BusinessJdbiTransaction
import mm.inventory.adapters.store.jdbi.units.UnitOfMeasurementJdbiRepository
import mm.inventory.domain.items.uc.CreateItemUseCase
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
    fun itemClassRepository() = ItemClassJdbiRepository(jdbi)

    @Bean
    fun itemRepository() = ItemJdbiRepository(jdbi, itemClassRepository())

    @Bean
    fun itemMutator() = ItemJdbiMutator(jdbi)

    @Bean
    fun unitOfMeasurementRepository() = UnitOfMeasurementJdbiRepository(jdbi)

    @Bean
    fun itemCreator() = CreateItemUseCase(businessTransaction(), securityGuard, itemClassRepository(), itemMutator())

    @Bean
    fun itemCrudRepository() = ItemCrudJdbiRepository(jdbi)

    @Bean
    fun businessTransaction() = BusinessJdbiTransaction(jdbi)
}