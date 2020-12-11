package mm.inventory.adapter.web.spring

import mm.inventory.app.itemsfacade.item.ItemCrudRepository
import mm.inventory.app.itemsfacade.item.ItemFacade
import mm.inventory.app.itemsfacade.itemclass.ItemClassFacade
import mm.inventory.domain.items.ItemClassRepository
import mm.inventory.domain.items.ItemRepository
import mm.inventory.domain.items.uc.CreateItemUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FacadeConfiguration(
    private val itemClassRepository: ItemClassRepository,
    private val itemRepository: ItemRepository,
    private val itemCrudRepository: ItemCrudRepository,
    private val itemCreator: CreateItemUseCase
) {
    @Bean
    fun itemClassFacade() = ItemClassFacade(itemClassRepository)

    @Bean
    fun itemFacade() = ItemFacade(itemRepository, itemCrudRepository, itemCreator)
}