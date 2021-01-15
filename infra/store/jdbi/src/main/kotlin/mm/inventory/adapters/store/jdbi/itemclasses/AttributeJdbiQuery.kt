package mm.inventory.adapters.store.jdbi.itemclasses

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import mm.inventory.app.productplanner.itemclass.AttributeHeader
import mm.inventory.app.productplanner.itemclass.AttributeQuery
import org.jdbi.v3.core.Jdbi

class AttributeJdbiQuery(private val db: Jdbi) : AttributeQuery {
    override fun findAll(): ImmutableList<AttributeHeader> =
        db.withHandle<ImmutableList<AttributeHeader>, RuntimeException> { handle ->
            handle.attach(AttributeTypeDao::class.java).selectAll().toImmutableList()
        }
}
