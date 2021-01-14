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
internal interface ItemClassDao {

    @SqlQuery("SELECT name, version, description, unit FROM Item_classes WHERE (name, version) IN (SELECT name, MAX(version) FROM Item_classes WHERE complete=TRUE GROUP BY name) ORDER BY name")
    fun selectItemClasses(): List<ItemClassRec>

    @SqlQuery("SELECT MAX(version) FROM Item_classes WHERE name=? AND complete=TRUE")
    fun selectNewestItemClassVersion(itemClassName: String): Long?

    @SqlQuery("SELECT MIN(version) FROM Item_classes WHERE name=? AND complete=FALSE")
    fun selectDraftVersion(itemClassName: String): Long?

    @SqlQuery("SELECT name, version, description, unit FROM Item_Classes WHERE name=? AND version=? ORDER BY version DESC")
    fun selectItemClassByName(itemClassName: String, itemClassVersion: Long): ItemClassRec?

    @SqlQuery("SELECT a.attribute_type, at.name, at.scalar, u.code as unit_code, u.name as unit_name FROM Attributes a JOIN Attribute_Types at ON a.attribute_type = at.name LEFT OUTER JOIN Units u ON at.unit = u.code WHERE item_class_name=? AND item_class_version=?")
    fun selectAttributesWithTypesForItemClass(itemClassName: String, itemClassVersion: Long): List<AttributeWithTypeRec>

    @SqlQuery("SELECT attribute_type_name, code, value FROM Attribute_Type_Values WHERE attribute_type_name IN (SELECT attribute_type FROM Attributes WHERE item_class_name=? AND item_class_version=?)")
    fun selectAttributeValuesForItemClass(itemClassName: String, itemClassVersion: Long): List<AttributeTypeValueRec>

    @SqlQuery("SELECT last_version FROM Item_classes_version_counters WHERE name=? FOR UPDATE")
    fun selectCounterAndLock(itemClassName: String): Long?

    @SqlUpdate("INSERT INTO Item_classes_version_counters VALUES (?, 1)")
    fun insertCounter(itemClassName: String): Int

    @SqlUpdate("UPDATE Item_classes_version_counters SET last_version=:lastVersion WHERE name=:itemClassName")
    fun updateCounter(itemClassName: String, lastVersion: Long): Int

    @SqlUpdate("UPDATE Item_classes_version_counters SET last_version=(SELECT MAX(version) FROM Item_classes WHERE name=:itemClassName) WHERE name=:itemClassName")
    fun revertCounter(itemClassName: String): Int

    @SqlUpdate("INSERT INTO Item_classes(name, version, complete, description, unit) VALUES (:itemClass.name, :itemClass.version, FALSE, :itemClass.description, :itemClass.unit)")
    fun insertItemClass(itemClass: ItemClassRec): Int

    @SqlUpdate("INSERT INTO Attributes(item_class_name, item_class_version, attribute_type) VALUES (:attribute.itemClassName, :attribute.itemClassVersion, :attribute.attributeType)")
    fun insertAttribute(attribute: AttributeRec): Int

    @SqlUpdate("DELETE FROM Attributes WHERE item_class_name=:attribute.itemClassName AND item_class_version=:attribute.itemClassVersion AND attribute_type=:attribute.attributeType")
    fun deleteAttribute(attribute: AttributeRec): Int

    @SqlUpdate("UPDATE Item_classes SET complete=TRUE WHERE name=:id.id AND version=:id.version AND complete=FALSE")
    fun completeDraftItemClass(id:JdbiItemClassId): Int

    @SqlUpdate("DELETE FROM Item_classes WHERE name=:id.id AND version=:id.version AND complete=FALSE")
    fun deleteDraftItemClass(id:JdbiItemClassId): Int

    @SqlUpdate("UPDATE Item_classes SET description = :description WHERE name=:id.id AND version=:id.version AND complete=FALSE")
    fun updateDescription(id:JdbiItemClassId, description: String): Int

    @SqlUpdate("UPDATE Item_classes SET unit=:unit WHERE name=:id.id AND version=:id.version AND complete=FALSE")
    fun updateUnit(id:JdbiItemClassId, unit: String): Int
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

data class AttributeRec(
    val itemClassName: String,
    val itemClassVersion: Long,
    val attributeType: String
)
