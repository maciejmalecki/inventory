package mm.inventory.adapter.web.spring

import mm.inventory.app.productplanner.item.ItemQuery
import mm.inventory.app.productplanner.item.ItemFacade
import mm.inventory.app.productplanner.itemclass.ItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassQuery
import mm.inventory.domain.items.itemclass.ItemClassSelector
import mm.inventory.domain.items.item.ItemSelector
import mm.inventory.domain.items.behaviors.CreateItem
import mm.inventory.domain.items.behaviors.UpdateItem
import mm.inventory.domain.items.item.ItemMutator
import mm.inventory.domain.shared.security.SecurityGuard
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FacadeConfiguration(
    private val securityGuard: SecurityGuard,
    private val itemClassSelector: ItemClassSelector,
    private val itemClassQuery: ItemClassQuery,
    private val itemSelector: ItemSelector,
    private val itemMutator: ItemMutator,
    private val itemQuery: ItemQuery,
    private val itemCreator: CreateItem,
    private val itemUpdater: UpdateItem
) {
    @Bean
    fun itemClassFacade() = ItemClassFacade(securityGuard, itemClassSelector, itemClassQuery)

    @Bean
    fun itemFacade() = ItemFacade(securityGuard, itemSelector, itemMutator, itemQuery, itemCreator, itemUpdater)
}