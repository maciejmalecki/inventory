package mm.inventory.adapters.store.jdbi.items

import org.jdbi.v3.core.Jdbi

data class ItemHeader(val name: String, val itemClassName: String)

class ItemCrudRepository(private val db: Jdbi) {

    fun selectItems(): List<ItemHeader> =
        db.withHandle<List<ItemHeader>, RuntimeException> { handle ->
            handle.attach(ItemDao::class.java).selectItems().map { itemRec ->
                ItemHeader(itemRec.name, itemRec.itemClassName)
            }
        }
}