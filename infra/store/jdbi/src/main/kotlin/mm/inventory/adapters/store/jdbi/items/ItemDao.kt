package mm.inventory.adapters.store.jdbi.items

import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.math.BigDecimal

interface ItemDao {

    @SqlUpdate("INSERT INTO Items(name, item_class_name, item_class_version) VALUES (:item.name, :item.itemClassName, :item.itemClassVersion)")
    fun insertItem(item: ItemRec)

    @SqlUpdate("INSERT INTO Scalar_Values(item_name, attribute_type, item_class_name, item_class_version, value, scale) VALUES (:value.itemName, :value.attributeType, :value.itemClassName, :value.itemClassVersion, :value.value, :value.scale)")
    fun insertValue(value: ScalarValueRec): Int

    @SqlUpdate("UPDATE Scalar_Values SET value=:value.value, scale=:value.scale WHERE item_name=:value.itemName AND attribute_type=:value.attributeType AND item_class_name=:value.itemClassName AND item_class_version=:value.itemClassVersion")
    fun updateValue(value: ScalarValueRec): Int

    @SqlUpdate("INSERT INTO Dictionary_Values(item_name, attribute_type, item_class_name, item_class_version, attribute_type_name, code) VALUES (:value.itemName, :value.attributeType, :value.itemClassName, :value.itemClassVersion, :value.attributeTypeName, :value.code)")
    fun insertValue(value: DictionaryValueRec): Int

    @SqlUpdate("UPDATE Dictionary_Values SET code=:value.code WHERE item_name=:value.itemName AND attribute_type=:value.attributeType AND item_class_name=:value.itemClassName AND item_class_version=:value.itemClassVersion")
    fun updateValue(value: DictionaryValueRec): Int

    @SqlQuery("SELECT name, item_class_name, item_class_version FROM Items ORDER BY name")
    fun selectItems(): List<ItemRec>

    @SqlQuery("SELECT name, item_class_name, item_class_version FROM Items WHERE name=?")
    fun selectItem(name: String): ItemRec?

    @SqlUpdate("DELETE FROM Items where name=?")
    fun deleteItem(name: String): Int

    @SqlQuery("SELECT item_name, attribute_type, item_class_name, item_class_version, value, scale FROM Scalar_values WHERE item_name=?")
    fun selectScalars(itemName: String): List<ScalarValueRec>

    @SqlUpdate("DELETE FROM Scalar_values WHERE item_name=?")
    fun deleteScalars(itemName: String): Int

    @SqlQuery("SELECT item_name, attribute_type, item_class_name, item_class_version, attribute_type_name, code FROM Dictionary_Values WHERE item_name=?")
    fun selectDictionaryValues(itemName: String): List<DictionaryValueRec>

    @SqlUpdate("DELETE FROM Dictionary_values WHERE item_name=?")
    fun deleteDictionaryValues(itemName: String): Int
}

data class ItemRec(
    val name: String,
    val itemClassName: String,
    val itemClassVersion: Long
)

data class ScalarValueRec(
    val itemName: String,
    val attributeType: String,
    val itemClassName: String,
    val itemClassVersion: Long,
    val value: BigDecimal?,
    val scale: Int
)

data class DictionaryValueRec(
    val itemName: String,
    val attributeType: String,
    val itemClassName: String,
    val itemClassVersion: Long,
    val attributeTypeName: String,
    val code: String?
)
