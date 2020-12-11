package mm.inventory.domain.items

interface ItemSelector {
    fun findByName(name: String): Item?
}