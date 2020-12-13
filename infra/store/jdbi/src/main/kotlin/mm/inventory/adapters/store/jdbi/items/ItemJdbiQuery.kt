package mm.inventory.adapters.store.jdbi.items

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import mm.inventory.app.productplanner.item.ItemQuery
import mm.inventory.app.productplanner.item.ItemHeader
import org.jdbi.v3.core.Jdbi

class ItemJdbiQuery(private val db: Jdbi) : ItemQuery {

    override fun findAll(): ImmutableList<ItemHeader> =
        db.withHandle<ImmutableList<ItemHeader>, RuntimeException> { handle ->
            handle.attach(ItemDao::class.java).selectItems().map { itemRec ->
                ItemHeader(itemRec.name, itemRec.itemClassName)
            }.toImmutableList()
        }
}