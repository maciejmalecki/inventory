package mm.inventory.infra.store.jdbi.items

import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.app.productplanner.manufacturer.ManufacturerAppId
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.customizer.DefineNamedBindings
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.stringtemplate4.UseStringTemplateEngine
import java.math.BigDecimal

interface ItemDao {

    @SqlUpdate("INSERT INTO Items(name, item_class_name, item_class_version, manufacturer_id, manufacturers_code) VALUES (:name, :itemClassName, :itemClassVersion, :manufacturerId, :manufacturersCode)")
    fun insertItem(@BindBean item: ItemRec)

    @SqlUpdate("INSERT INTO Scalar_Values(item_name, attribute_type, item_class_name, item_class_version, value, scale) VALUES (:itemName, :attributeType, :itemClassName, :itemClassVersion, :value, :scale)")
    fun insertValue(@BindBean value: ScalarValueRec): Int

    @SqlUpdate("UPDATE Scalar_Values SET value=:value, scale=:scale WHERE item_name=:itemName AND attribute_type=:attributeType AND item_class_name=:itemClassName AND item_class_version=:itemClassVersion")
    fun updateValue(@BindBean value: ScalarValueRec): Int

    @SqlUpdate("INSERT INTO Dictionary_Values(item_name, attribute_type, item_class_name, item_class_version, attribute_type_name, code) VALUES (:itemName, :attributeType, :itemClassName, :itemClassVersion, :attributeTypeName, :code)")
    fun insertValue(@BindBean value: DictionaryValueRec): Int

    @SqlUpdate("UPDATE Dictionary_Values SET code=:code WHERE item_name=:itemName AND attribute_type=:attributeType AND item_class_name=:itemClassName AND item_class_version=:itemClassVersion")
    fun updateValue(@BindBean value: DictionaryValueRec): Int

    @SqlQuery("SELECT items.name AS name, item_class_name, item_class_version, manufacturer_id, manufacturers.name AS manufacturer_name, manufacturers_code FROM Items LEFT OUTER JOIN Manufacturers ON items.manufacturer_id = manufacturers.id ORDER BY name")
    fun selectItems(): List<ItemWithManufacturerRec>

    @SqlQuery(
        """
        SELECT 
            items.name AS name, item_class_name, item_class_version, manufacturer_id, manufacturers.name AS manufacturer_name, manufacturers_code 
        FROM 
            Items LEFT OUTER JOIN Manufacturers ON items.manufacturer_id = manufacturers.id 
        WHERE 
            1=1 
            <if(name)>AND items.name LIKE :name<endif> 
            <if(manufacturersCode)>AND manufacturers_code LIKE :manufacturersCode<endif> 
            <if(manufacturerIds)>AND manufacturer_id IN (<manufacturerIds>)<endif>
            <if(itemClassIds)>AND item_class_name IN (<itemClassIds>)<endif>
        ORDER BY name"""
    )
    @DefineNamedBindings
    @UseStringTemplateEngine
    fun selectItemsByCriteria(
        @BindBean criteria: ItemSearchJdbiCriteria,
        @BindList("manufacturerIds", onEmpty = BindList.EmptyHandling.NULL_VALUE) manufacturerIds: List<Long>?,
        @BindList("itemClassIds", onEmpty = BindList.EmptyHandling.NULL_VALUE) itemClassIds: List<String>?
    ): List<ItemWithManufacturerRec>

    @SqlQuery("SELECT items.name AS name, item_class_name, item_class_version, manufacturer_id, manufacturers.name AS manufacturer_name, manufacturers_code FROM Items LEFT OUTER JOIN Manufacturers ON items.manufacturer_id = manufacturers.id WHERE items.name=?")
    fun selectItem(name: String): ItemWithManufacturerRec?

    @SqlUpdate("UPDATE Items SET manufacturer_id=:manufacturerId.id, manufacturers_code=:manufacturersCode WHERE name=:itemId.id")
    fun updateManufacturer(manufacturerId: ManufacturerAppId, manufacturersCode: String?, itemId: ItemAppId): Int

    @SqlUpdate("UPDATE Items SET manufacturer_id=NULL, manufacturers_code=NULL WHERE name=:itemId.id")
    fun removeManufacturer(itemId: ItemAppId): Int

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
