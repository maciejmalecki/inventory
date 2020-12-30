package mm.inventory.adapter.web.spring

import mm.inventory.app.productplanner.item.ItemQuery
import mm.inventory.app.productplanner.item.ItemFacade
import mm.inventory.app.productplanner.itemclass.ItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassQuery
import mm.inventory.domain.items.itemclass.ItemClassSelector
import mm.inventory.domain.items.item.ItemRepository
import mm.inventory.domain.items.item.ItemFactory
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FacadeConfiguration(
    private val securityGuard: SecurityGuard,
    private val businessTransaction: BusinessTransaction,
    private val itemClassSelector: ItemClassSelector,
    private val itemClassQuery: ItemClassQuery,
    private val itemRepository: ItemRepository,
    private val itemQuery: ItemQuery,
    private val itemFactoryCreator: ItemFactory
) {
    @Bean
    fun itemClassFacade() = ItemClassFacade(securityGuard, itemClassSelector, itemClassQuery)

    @Bean
    fun itemFacade() = ItemFacade(securityGuard, businessTransaction, itemRepository, itemQuery, itemFactoryCreator)
}