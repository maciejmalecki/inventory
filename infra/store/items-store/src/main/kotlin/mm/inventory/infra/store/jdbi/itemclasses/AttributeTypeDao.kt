package mm.inventory.infra.store.jdbi.itemclasses

import mm.inventory.app.productplanner.itemclass.AttributeHeader
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface AttributeTypeDao {

    @SqlQuery("SELECT Attribute_types.name AS name, scalar, Units.code AS unit_code, Units.name AS unit_name  FROM Attribute_types LEFT OUTER JOIN Units ON attribute_types.unit=Units.code ORDER BY name")
    fun selectAll(): List<AttributeHeader>

    @SqlQuery("SELECT a.name, a.scalar, u.code AS unit_code, u.name AS unit_name, v.code, v.value FROM Attribute_types a LEFT OUTER JOIN Attribute_type_values v ON a.name = v.attribute_type_name LEFT OUTER JOIN Units u ON a.unit = u.code WHERE a.name=:name ORDER BY a.name, v.code")
    fun selectAttributeType(name: String): List<AttributeTypeRec>
}

data class AttributeTypeRec(
    val name: String,
    val scalar: Boolean,
    val unitCode: String?,
    val unitName: String?,
    val code: String?,
    val value: String?
)
