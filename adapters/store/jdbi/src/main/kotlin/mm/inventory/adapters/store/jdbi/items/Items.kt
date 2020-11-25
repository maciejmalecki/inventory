package mm.inventory.adapters.store.jdbi.items

data class ItemClassRec (val name: String, val description: String, val unit: String)
data class AttributeTypeRec (val name: String, val unit: String, val scalar: Boolean)
data class AttributeTypeValueRec (val attributeTypeName: String, val value: String)
data class AttributeRec (val name: String, val itemClassName: String, val attributeType: String)
data class AttributeWithTypeRec (val name: String, val attributeType: String, val scalar: Boolean, val unitCode: String?, val unitName: String?)