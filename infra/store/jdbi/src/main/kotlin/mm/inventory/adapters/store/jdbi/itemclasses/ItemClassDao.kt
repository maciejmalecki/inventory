package mm.inventory.adapters.store.jdbi.itemclasses

import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

/**
 * Dao to access Item_Classes table and relatives.
 * -------------------------------------------------------------------------------------------------------------------------------------------
 * Note: This Dao class is just a declarative way of accessing data in Relational Database using JDBI. It shouldn't be mixed with repositories
 * which are domain related artifacts, therefore different naming is used.
 * -------------------------------------------------------------------------------------------------------------------------------------------
 */
interface ItemClassDao {

    @SqlQuery("SELECT name, version, description, unit FROM Item_classes WHERE (name, version) IN (SELECT name, MAX(version) FROM Item_classes GROUP BY name) ORDER BY name")
    fun selectItemClasses(): List<ItemClassRec>

    @SqlQuery("SELECT MAX(version) FROM Item_classes WHERE name=?")
    fun selectNewestItemClassVersion(itemClassName: String): Long?

    @SqlQuery("SELECT name, version, description, unit FROM Item_Classes WHERE name=? AND version=? ORDER BY version DESC")
    fun selectItemClassByName(itemClassName: String, itemClassVersion: Long): ItemClassRec?

    @SqlQuery("SELECT a.attribute_type, at.name, at.scalar, u.code as unit_code, u.name as unit_name FROM Attributes a JOIN Attribute_Types at ON a.attribute_type = at.name LEFT OUTER JOIN Units u ON at.unit = u.code WHERE item_class_name=? AND item_class_version=?")
    fun selectAttributesWithTypesForItemClass(itemClassName: String, itemClassVersion: Long): List<AttributeWithTypeRec>

    @SqlQuery("SELECT attribute_type_name, code, value FROM Attribute_Type_Values WHERE attribute_type_name IN (SELECT attribute_type FROM Attributes WHERE item_class_name=? AND item_class_version=?)")
    fun selectAttributeValuesForItemClass(itemClassName: String, itemClassVersion: Long): List<AttributeTypeValueRec>

    @SqlQuery("SELECT last_version FROM Item_classes_version_counters FOR UPDATE")
    fun selectCounter(itemClassName: String): Long?

    @SqlUpdate("INSERT INTO Item_classes_version_counters VALUES (?, 1)")
    fun insertCounter(itemClassName: String)

    @SqlUpdate("UPDATE Item_classes_version_counters SET last_version=:lastVersion WHERE name=:itemClassName")
    fun updateCounter(itemClassName: String, lastVersion: Long)

    @SqlUpdate("INSERT INTO Item_classes(name, version, complete, description, unit) VALUES (:itemClass.name, :itemClass.version, FALSE, :itemClass.description, :itemClass.unit)")
    fun insertItemClass(itemClass: ItemClassRec): Int

    @SqlUpdate("UPDATE Item_classes SET complete=TRUE WHERE name=? AND version=? AND complete=FALSE")
    fun completeDraftItemClass(itemClassName: String, version: Long): Int
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
