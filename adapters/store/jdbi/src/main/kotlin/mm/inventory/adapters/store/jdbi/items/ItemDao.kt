package mm.inventory.adapters.store.jdbi.items

import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.math.BigDecimal

interface ItemDao {

    @SqlUpdate("INSERT INTO Items(name, item_class_name) VALUES (:item.name, :item.itemClassName)")
    fun insertItem(item: ItemRec)

    @SqlUpdate("INSERT INTO Scalar_Values(item_name, attribute_type, item_class_name, value, scale) VALUES (:value.itemName, :value.attributeType, :value.itemClassName, :value:value, :value.scale)")
    fun insertValue(value: ScalarValueRec)

    @SqlUpdate("INSERT INTO Dictionary_Values(item_name, attribute_type, item_class_name, attribute_type_name, code) VALUES (:value:itemName, :value.attributeType, :value.itemClassName, :value.attributeTypeName, :value.code)")
    fun insertValue(value: DictionaryValueRec)
}

data class ItemRec(val name: String, val itemClassName: String)
data class ScalarValueRec(val itemName: String, val attributeType: String, val itemClassName: String, val value: BigDecimal?, val scale: Int)
data class DictionaryValueRec(val itemName: String, val attributeType: String, val itemClassName: String, val attributeTypeName: String, val code: String?)
