package mm.inventory.app.itemsfacade.item

import kotlinx.collections.immutable.ImmutableMap

interface ItemCrudRepository {

    fun update(item: String, inValues: ImmutableMap<String, String>)
}