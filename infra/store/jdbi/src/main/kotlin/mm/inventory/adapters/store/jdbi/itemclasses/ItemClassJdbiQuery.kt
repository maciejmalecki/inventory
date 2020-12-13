package mm.inventory.adapters.store.jdbi.itemclasses

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import mm.inventory.app.productplanner.itemclass.ItemClassHeader
import mm.inventory.app.productplanner.itemclass.ItemClassQuery
import org.jdbi.v3.core.Jdbi
import java.lang.RuntimeException

class ItemClassJdbiQuery(private val db: Jdbi) : ItemClassQuery {
    override fun findAll(): ImmutableList<ItemClassHeader> =
        db.withHandle<ImmutableList<ItemClassHeader>, RuntimeException> { handle ->
            handle.attach(ItemClassDao::class.java).selectItemClasses().map { rec ->
                ItemClassHeader(rec.name, rec.description)
            }.toImmutableList()
        }
}