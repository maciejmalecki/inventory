package mm.inventory.adapter.web.spring.conf.jdbi

import mm.inventory.infra.store.inventory.stock.ItemStockJdbiQuery
import mm.inventory.infra.store.inventory.stock.ItemStockJdbiRepository
import mm.inventory.infra.store.jdbi.itemclasses.AttributeJdbiQuery
import mm.inventory.infra.store.jdbi.itemclasses.AttributeJdbiRepository
import mm.inventory.infra.store.jdbi.itemclasses.DraftItemClassJdbiRepository
import mm.inventory.infra.store.jdbi.itemclasses.ItemClassJdbiQuery
import mm.inventory.infra.store.jdbi.itemclasses.ItemClassJdbiRepository
import mm.inventory.infra.store.jdbi.items.ItemJdbiQuery
import mm.inventory.infra.store.jdbi.items.ItemJdbiRepository
import mm.inventory.infra.store.jdbi.items.ManufacturerCrudJdbiRepository
import mm.inventory.infra.store.jdbi.transactions.BusinessJdbiTransaction
import mm.inventory.infra.store.jdbi.units.UnitOfMeasurementJdbiRepository
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
    fun attributeTypeQuery() = AttributeJdbiQuery(jdbi)

    @Bean
    fun attributeTypeRepository() = AttributeJdbiRepository(jdbi)

    @Bean
    fun manufacturerCrudRepository() = ManufacturerCrudJdbiRepository(jdbi)

    @Bean
    fun itemStockRepository() = ItemStockJdbiRepository(jdbi)

    @Bean
    fun itemStockQuery() = ItemStockJdbiQuery(jdbi)
}