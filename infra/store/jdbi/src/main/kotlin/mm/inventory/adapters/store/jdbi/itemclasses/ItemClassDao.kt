package mm.inventory.adapters.store.jdbi.itemclasses

import org.jdbi.v3.sqlobject.statement.SqlQuery

/**
 * Dao to access Item_Classes table and relatives.
 * -------------------------------------------------------------------------------------------------------------------------------------------
 * Note: This Dao class is just a declarative way of accessing data in Relational Database using JDBI. It shouldn't be mixed with repositories
 * which are domain related artifacts, therefore different naming is used.
 * -------------------------------------------------------------------------------------------------------------------------------------------
 */
interface ItemClassDao {

    @SqlQuery("SELECT name, version, description, unit FROM Item_classes ORDER BY name")
    fun selectItemClasses(): List<ItemClassRec>

    @SqlQuery("SELECT name, version, description, unit FROM Item_Classes WHERE name=? ORDER BY version DESC")
    fun selectItemClassByName(itemClassName: String): ItemClassRec?

    @SqlQuery("SELECT a.attribute_type, at.name, at.scalar, u.code as unit_code, u.name as unit_name FROM Attributes a JOIN Attribute_Types at ON a.attribute_type = at.name LEFT OUTER JOIN Units u ON at.unit = u.code WHERE item_class_name=? AND item_class_version=?")
    fun selectAttributesWithTypesForItemClass(itemClassName: String, itemClassVersion: Long): List<AttributeWithTypeRec>

    @SqlQuery("SELECT attribute_type_name, code, value FROM Attribute_Type_Values WHERE attribute_type_name IN (SELECT attribute_type FROM Attributes WHERE item_class_name=? AND item_class_version=?)")
    fun selectAttributeValuesForItemClass(itemClassName: String, itemClassVersion: Long): List<AttributeTypeValueRec>
}

data class ItemClassRec(
    val name: String,
    val version: Long,
    val description: String,
    val unit: String
)

data class AttributeWithTypeRec(
    val attributeType: String,
    val name: String,
    val scalar: Boolean,
    val unitCode: String?,
    val unitName: String?
)

data class AttributeTypeValueRec(
    val attributeTypeName: String,
    val code: String,
    val value: String
)
