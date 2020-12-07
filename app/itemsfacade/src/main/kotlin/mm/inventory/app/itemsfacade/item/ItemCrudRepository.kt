package mm.inventory.app.itemsfacade.item

data class ItemHeader(val name: String, val itemClassName: String)

interface ItemCrudRepository {
    fun selectItems(): List<ItemHeader>
}