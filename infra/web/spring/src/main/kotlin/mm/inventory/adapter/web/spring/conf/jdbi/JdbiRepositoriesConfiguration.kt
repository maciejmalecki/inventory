package mm.inventory.adapter.web.spring.conf.jdbi

import mm.inventory.adapters.store.jdbi.itemclasses.AttributeTypeJdbiQuery
import mm.inventory.adapters.store.jdbi.itemclasses.DraftItemClassJdbiRepository
import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiQuery
import mm.inventory.adapters.store.jdbi.itemclasses.ItemClassJdbiRepository
import mm.inventory.adapters.store.jdbi.items.ItemJdbiQuery
import mm.inventory.adapters.store.jdbi.items.ItemJdbiRepository
import mm.inventory.adapters.store.jdbi.transactions.BusinessJdbiTransaction
import mm.inventory.adapters.store.jdbi.units.UnitOfMeasurementJdbiRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JdbiRepositoriesConfiguration(
    private val jdbi: Jdbi
) {
    @Bean
    fun itemClassRepository() = ItemClassJdbiRepository(jdbi)

    @Bean
    fun draftItemClassRepository() = DraftItemClassJdbiRepository(jdbi, itemClassRepository())

    @Bean
    fun itemClassQuery() = ItemClassJdbiQuery(jdbi)

    @Bean
    fun itemRepository() = ItemJdbiRepository(jdbi, itemClassRepository())

    @Bean
    fun unitOfMeasurementRepository() = UnitOfMeasurementJdbiRepository(jdbi)

    @Bean
    fun itemCrudQuery() = ItemJdbiQuery(jdbi)

    @Bean
    fun businessTransaction() = BusinessJdbiTransaction(jdbi)

    @Bean
    fun attributeTypeQuery() = AttributeTypeJdbiQuery(jdbi)
}