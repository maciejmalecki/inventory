package mm.inventory.adapter.web.spring

import mm.inventory.app.itemsfacade.item.ItemQuery
import mm.inventory.app.itemsfacade.item.ItemFacade
import mm.inventory.app.itemsfacade.itemclass.ItemClassFacade
import mm.inventory.app.itemsfacade.itemclass.ItemClassQuery
import mm.inventory.domain.items.ItemClassSelector
import mm.inventory.domain.items.ItemSelector
import mm.inventory.domain.items.behaviors.CreateItem
import mm.inventory.domain.items.behaviors.UpdateItem
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FacadeConfiguration(
    private val itemClassSelector: ItemClassSelector,
    private val itemClassQuery: ItemClassQuery,
    private val itemSelector: ItemSelector,
    private val itemQuery: ItemQuery,
    private val itemCreator: CreateItem,
    private val itemUpdater: UpdateItem
) {
    @Bean
    fun itemClassFacade() = ItemClassFacade(itemClassSelector, itemClassQuery)

    @Bean
    fun itemFacade() = ItemFacade(itemSelector, itemQuery, itemCreator, itemUpdater)
}