package mm.inventory.adapters.store.jdbi.itemclasses

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import mm.inventory.app.productplanner.itemclass.AttributeTypeHeader
import mm.inventory.app.productplanner.itemclass.AttributeTypeQuery
import org.jdbi.v3.core.Jdbi

class AttributeTypeJdbiQuery(private val db: Jdbi) : AttributeTypeQuery {
    override fun findAll(): ImmutableList<AttributeTypeHeader> =
        db.withHandle<ImmutableList<AttributeTypeHeader>, RuntimeException> { handle ->
            handle.attach(AttributeTypeDao::class.java).selectAll().toImmutableList()
        }
}
