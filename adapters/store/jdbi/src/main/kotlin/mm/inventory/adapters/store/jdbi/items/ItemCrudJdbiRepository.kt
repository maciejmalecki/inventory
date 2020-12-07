package mm.inventory.adapters.store.jdbi.items

import mm.inventory.app.itemsfacade.item.ItemCrudRepository
import mm.inventory.app.itemsfacade.item.ItemHeader
import org.jdbi.v3.core.Jdbi

class ItemCrudJdbiRepository(private val db: Jdbi) : ItemCrudRepository {

    override fun selectItems(): List<ItemHeader> = db.withHandle<List<ItemHeader>, RuntimeException> { handle ->
        handle.attach(ItemDao::class.java).selectItems().map { itemRec ->
            ItemHeader(itemRec.name, itemRec.itemClassName)
        }
    }
}