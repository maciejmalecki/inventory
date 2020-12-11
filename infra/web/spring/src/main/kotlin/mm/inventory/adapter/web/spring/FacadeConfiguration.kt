package mm.inventory.adapter.web.spring

import mm.inventory.app.itemsfacade.item.ItemCrudRepository
import mm.inventory.app.itemsfacade.item.ItemFacade
import mm.inventory.app.itemsfacade.itemclass.ItemClassFacade
import mm.inventory.domain.items.ItemClassSelector
import mm.inventory.domain.items.ItemSelector
import mm.inventory.domain.items.behaviors.CreateItem
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FacadeConfiguration(
    private val itemClassSelector: ItemClassSelector,
    private val itemSelector: ItemSelector,
    private val itemCrudRepository: ItemCrudRepository,
    private val itemCreator: CreateItem
) {
    @Bean
    fun itemClassFacade() = ItemClassFacade(itemClassSelector)

    @Bean
    fun itemFacade() = ItemFacade(itemSelector, itemCrudRepository, itemCreator)
}