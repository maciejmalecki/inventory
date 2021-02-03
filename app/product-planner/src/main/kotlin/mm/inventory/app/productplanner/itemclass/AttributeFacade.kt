package mm.inventory.app.productplanner.itemclass

class AttributeFacade(private val attributeQuery: AttributeQuery) {
    fun findAll() = attributeQuery.findAll()
}