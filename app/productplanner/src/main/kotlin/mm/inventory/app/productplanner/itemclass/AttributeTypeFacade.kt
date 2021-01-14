package mm.inventory.app.productplanner.itemclass

class AttributeTypeFacade(private val attributeTypeQuery: AttributeTypeQuery) {
    fun findAll() = attributeTypeQuery.findAll()
}