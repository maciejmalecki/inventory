package mm.inventory.adapter.web.spring

import mm.inventory.app.productplanner.item.ItemQuery
import mm.inventory.app.productplanner.item.ItemFacade
import mm.inventory.app.productplanner.itemclass.ItemClassFacade
import mm.inventory.app.productplanner.itemclass.ItemClassQuery
import mm.inventory.domain.items.itemclass.ItemClassSelector
import mm.inventory.domain.items.item.ItemSelector
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