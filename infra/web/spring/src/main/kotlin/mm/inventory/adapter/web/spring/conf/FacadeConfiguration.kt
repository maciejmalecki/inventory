package mm.inventory.adapter.web.spring.conf

import mm.inventory.app.productplanner.item.ItemQuery
import mm.inventory.app.productplanner.item.ItemFacade
import mm.inventory.app.productplanner.manufacturer.ManufacturerCrudRepository
import mm.inventory.app.productplanner.itemclass.AttributeFacade
import mm.inventory.app.productplanner.itemclass.AttributeQuery
import mm.inventory.app.productplanner.itemclass.DraftItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassQuery
import mm.inventory.app.productplanner.manufacturer.ManufacturerFacade
import mm.inventory.app.productplanner.stock.StockFacade
import mm.inventory.domain.inventory.stock.ItemStockRepository
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.items.item.ItemRepository
import mm.inventory.domain.items.item.ItemFactory
import mm.inventory.domain.items.itemclass.AttributeRepository
import mm.inventory.domain.items.itemclass.DraftItemClassFactory
import mm.inventory.domain.items.itemclass.DraftItemClassManager
import mm.inventory.domain.items.itemclass.DraftItemClassRepository
import mm.inventory.domain.items.itemclass.UnitOfMeasurementRepository
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FacadeConfiguration(
    private val securityGuard: SecurityGuard,
    private val businessTransaction: BusinessTransaction,
    private val itemClassRepository: ItemClassRepository,
    private val draftItemClassRepository: DraftItemClassRepository,
    private val itemClassQuery: ItemClassQuery,
    private val itemRepository: ItemRepository,
    private val itemQuery: ItemQuery,
    private val itemFactoryCreator: ItemFactory,
    private val draftItemClassFactory: DraftItemClassFactory,
    private val draftItemClassManager: DraftItemClassManager,
    private val unitOfMeasurementRepository: UnitOfMeasurementRepository,
    private val attributeQuery: AttributeQuery,
    private val attributeRepository: AttributeRepository,
    private val manufacturerCrudRepository: ManufacturerCrudRepository,
    private val itemStockRepository: ItemStockRepository
) {
    @Bean
    fun itemClassFacade() = ItemClassFacade(
        securityGuard,
        itemClassRepository,
        itemClassQuery
    )

    @Bean
    fun draftItemClassFacade() = DraftItemClassFacade(
        securityGuard,
        businessTransaction,
        draftItemClassRepository,
        draftItemClassFactory,
        draftItemClassManager,
        unitOfMeasurementRepository,
        itemClassRepository,
        attributeRepository
    )

    @Bean
    fun itemFacade() = ItemFacade(
        securityGuard,
        businessTransaction,
        itemRepository,
        itemQuery,
        itemFactoryCreator,
        manufacturerCrudRepository
    )

    @Bean
    fun attributeFacade() = AttributeFacade(attributeQuery)

    @Bean
    fun manufacturerFacade() = ManufacturerFacade(manufacturerCrudRepository)

    @Bean
    fun stockFacade() = StockFacade(securityGuard, businessTransaction, itemStockRepository)
}