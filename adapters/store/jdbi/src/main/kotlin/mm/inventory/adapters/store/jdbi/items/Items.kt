package mm.inventory.adapters.store.jdbi.items

data class ItemClassRec (val name: String, val description: String, val unit: String)
data class AttributeTypeValueRec (val attributeTypeName: String, val code: String, val value: String)
data class AttributeWithTypeRec (val attributeType: String, val name: String, val scalar: Boolean, val unitCode: String?, val unitName: String?)