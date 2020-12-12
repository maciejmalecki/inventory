package mm.inventory.adapters.store.jdbi.items

import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.math.BigDecimal

interface ItemDao {

    @SqlUpdate("INSERT INTO Items(name, item_class_name) VALUES (:item.name, :item.itemClassName)")
    fun insertItem(item: ItemRec)

    @SqlUpdate("INSERT INTO Scalar_Values(item_name, attribute_type, item_class_name, value, scale) VALUES (:value.itemName, :value.attributeType, :value.itemClassName, :value.value, :value.scale)")
    fun insertValue(value: ScalarValueRec): Int

    @SqlUpdate("UPDATE Scalar_Values SET value=:value.value, scale=:value.scale WHERE item_name=:value.itemName AND attribute_type=:value.attributeType AND item_class_name=:value.itemClassName")
    fun updateValue(value: ScalarValueRec): Int

    @SqlUpdate("INSERT INTO Dictionary_Values(item_name, attribute_type, item_class_name, attribute_type_name, code) VALUES (:value.itemName, :value.attributeType, :value.itemClassName, :value.attributeTypeName, :value.code)")
    fun insertValue(value: DictionaryValueRec): Int

    @SqlUpdate("UPDATE Dictionary_Values SET code=:value.code WHERE item_name=:value.itemName AND attribute_type=:value.attributeType AND item_class_name=:value.itemClassName")
    fun updateValue(value: DictionaryValueRec): Int

    @SqlQuery("SELECT name, item_class_name FROM Items ORDER BY name")
    fun selectItems(): List<ItemRec>

    @SqlQuery("SELECT name, item_class_name FROM Items WHERE name=?")
    fun selectItem(name: String): ItemRec?

    @SqlQuery("SELECT item_name, attribute_type, item_class_name, value, scale FROM Scalar_values WHERE item_name=?")
    fun selectScalars(itemName: String): List<ScalarValueRec>

    @SqlQuery("SELECT item_name, attribute_type, item_class_name, attribute_type_name, code FROM Dictionary_Values WHERE item_name=?")
    fun selectDictionaryValues(itemName: String): List<DictionaryValueRec>
}

data class ItemRec(val name: String, val itemClassName: String)
data class ScalarValueRec(val itemName: String, val attributeType: String, val itemClassName: String, val value: BigDecimal?, val scale: Int)
data class DictionaryValueRec(val itemName: String, val attributeType: String, val itemClassName: String, val attributeTypeName: String, val code: String?)
