package mm.inventory.app.itemsfacade.item

import kotlinx.collections.immutable.ImmutableMap
import mm.inventory.domain.items.uc.CreateItemUseCase
import mm.inventory.domain.items.ItemSelector

class ItemFacade(
    private val itemSelector: ItemSelector,
    private val itemCrudRepository: ItemCrudRepository,
    private val createItemUseCase: CreateItemUseCase
) {
    fun findAllItems() = itemCrudRepository.selectItems()

    fun findByName(name: String) = itemSelector.findByName(name)

    fun createItem(name: String, itemClassName: String, inValues: ImmutableMap<String, String>) =
        createItemUseCase.execute(name, itemClassName, inValues)
}