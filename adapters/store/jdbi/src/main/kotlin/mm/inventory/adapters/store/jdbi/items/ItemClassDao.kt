package mm.inventory.adapters.store.jdbi.items

import org.jdbi.v3.sqlobject.statement.SqlQuery

interface ItemClassDao {

    @SqlQuery("SELECT name, description, unit FROM Item_Classes WHERE name=?")
    fun findByName(itemClassName: String): ItemClassRec?

    @SqlQuery("SELECT a.name, a.attribute_type, at.scalar, u.code as unit_code, u.name as unit_name FROM Attributes a JOIN Attribute_Types at ON a.attribute_type = at.name LEFT OUTER JOIN Units u ON at.unit = u.code WHERE item_class_name=?")
    fun findAttributesWithTypesForItemClass(itemClassName: String): List<AttributeWithTypeRec>

    @SqlQuery("SELECT attribute_type_name, code, value FROM Attribute_Type_Values WHERE attribute_type_name IN (SELECT attribute_type FROM Attributes WHERE item_class_name=?)")
    fun findAttributeValuesForItemClass(itemClassName: String): List<AttributeTypeValueRec>
}