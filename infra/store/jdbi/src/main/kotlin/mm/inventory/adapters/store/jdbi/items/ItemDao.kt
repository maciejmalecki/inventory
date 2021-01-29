package mm.inventory.adapters.store.jdbi.items

import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.app.productplanner.manufacturer.ManufacturerAppId
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.math.BigDecimal

interface ItemDao {

    @SqlUpdate("INSERT INTO Items(name, item_class_name, item_class_version, manufacturer_id, manufacturers_code) VALUES (:item.name, :item.itemClassName, :item.itemClassVersion, :item.manufacturerId, :item.manufacturersCode)")
    fun insertItem(item: ItemRec)

    @SqlUpdate("INSERT INTO Scalar_Values(item_name, attribute_type, item_class_name, item_class_version, value, scale) VALUES (:value.itemName, :value.attributeType, :value.itemClassName, :value.itemClassVersion, :value.value, :value.scale)")
    fun insertValue(value: ScalarValueRec): Int

    @SqlUpdate("UPDATE Scalar_Values SET value=:value.value, scale=:value.scale WHERE item_name=:value.itemName AND attribute_type=:value.attributeType AND item_class_name=:value.itemClassName AND item_class_version=:value.itemClassVersion")
    fun updateValue(value: ScalarValueRec): Int

    @SqlUpdate("INSERT INTO Dictionary_Values(item_name, attribute_type, item_class_name, item_class_version, attribute_type_name, code) VALUES (:value.itemName, :value.attributeType, :value.itemClassName, :value.itemClassVersion, :value.attributeTypeName, :value.code)")
    fun insertValue(value: DictionaryValueRec): Int

    @SqlUpdate("UPDATE Dictionary_Values SET code=:value.code WHERE item_name=:value.itemName AND attribute_type=:value.attributeType AND item_class_name=:value.itemClassName AND item_class_version=:value.itemClassVersion")
    fun updateValue(value: DictionaryValueRec): Int

    @SqlQuery("SELECT items.name AS name, item_class_name, item_class_version, manufacturer_id, manufacturers.name AS manufacturer_name, manufacturers_code FROM Items LEFT OUTER JOIN Manufacturers ON items.manufacturer_id = manufacturers.id ORDER BY name")
    fun selectItems(): List<ItemWithManufacturerRec>

    @SqlQuery("SELECT items.name AS name, item_class_name, item_class_version, manufacturer_id, manufacturers.name AS manufacturer_name, manufacturers_code FROM Items LEFT OUTER JOIN Manufacturers ON items.manufacturer_id = manufacturers.id WHERE items.name=?")
    fun selectItem(name: String): ItemWithManufacturerRec?

    @SqlUpdate("UPDATE Items SET manufacturer_id=:manufacturerId.id WHERE name=:itemId.id")
    fun updateManufacturerId(manufacturerId: ManufacturerAppId, itemId: ItemAppId): Int

    @SqlUpdate("UPDATE Items SET manufacturer_id=NULL WHERE name=:itemId.id")
    fun removeManufacturerId(itemId: ItemAppId): Int

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
    val itemClassVersion: Long,
    val manufacturerId: Long?,
    val manufacturersCode: String?
)

data class ItemWithManufacturerRec(
    val name: String,
    val itemClassName: String,
    val itemClassVersion: Long,
    val manufacturerId: Long?,
    val manufacturerName: String?,
    val manufacturersCode: String?
);

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
