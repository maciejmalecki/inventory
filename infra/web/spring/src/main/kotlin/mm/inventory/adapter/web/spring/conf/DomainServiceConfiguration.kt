package mm.inventory.adapter.web.spring.conf

import mm.inventory.domain.items.item.ItemFactory
import mm.inventory.domain.items.item.ItemRepository
import mm.inventory.domain.items.itemclass.DraftItemClassFactory
import mm.inventory.domain.items.itemclass.DraftItemClassManager
import mm.inventory.domain.items.itemclass.DraftItemClassRepository
import mm.inventory.domain.items.itemclass.ItemClassRepository
import mm.inventory.domain.shared.transactions.BusinessTransaction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DomainServiceConfiguration(
    private val businessTransaction: BusinessTransaction,
    private val itemClassRepository: ItemClassRepository,
    private val draftItemClassRepository: DraftItemClassRepository,
    private val itemRepository: ItemRepository
) {
    @Bean
    fun itemCreator() = ItemFactory(businessTransaction, itemClassRepository, itemRepository)

    @Bean
    fun draftItemClassFactory() =
        DraftItemClassFactory(businessTransaction, itemClassRepository, draftItemClassRepository)

    @Bean
    fun draftItemClassManager() = DraftItemClassManager(businessTransaction, draftItemClassRepository)
}