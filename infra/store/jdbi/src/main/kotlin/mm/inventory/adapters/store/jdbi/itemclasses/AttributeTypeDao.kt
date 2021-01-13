package mm.inventory.adapters.store.jdbi.itemclasses

import mm.inventory.app.productplanner.itemclass.AttributeTypeHeader
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface AttributeTypeDao {

    @SqlQuery("SELECT name, scalar FROM Attribute_types ORDER BY name")
    fun selectAll(): List<AttributeTypeHeader>
}