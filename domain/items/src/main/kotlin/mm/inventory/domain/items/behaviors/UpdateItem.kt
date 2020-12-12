package mm.inventory.domain.items.behaviors

import kotlinx.collections.immutable.ImmutableMap
import mm.inventory.domain.items.DictionaryValue
import mm.inventory.domain.items.ITEMS_ROLE
import mm.inventory.domain.items.ITEMS_WRITER_ROLE
import mm.inventory.domain.items.Item
import mm.inventory.domain.items.ItemMutator
import mm.inventory.domain.items.ItemSelector
import mm.inventory.domain.items.ScalarValue
import mm.inventory.domain.items.Value
import mm.inventory.domain.items.parse
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction

class UpdateItem(
    private val tx: BusinessTransaction,
    private val sec: SecurityGuard,
    private val itemSelector: ItemSelector,
    private val itemMutator: ItemMutator
) {

    fun execute(name: String, inValues: ImmutableMap<String, String>) =
        sec.requireAllRoles(ITEMS_ROLE, ITEMS_WRITER_ROLE) {
            tx.inTransaction {
                val item = itemSelector.get(name)
                val itemClass = item.itemClass
                inValues.entries.forEach { (attributeName, value) ->
                    val attribute = itemClass.getAttribute(attributeName)
                    val attributeValue = attribute.parse(value)
                    updateValue(item, attributeValue)
                }
            }
        }

    private fun updateValue(item: Item, attributeValue: Value<*>) {
        when (attributeValue) {
            is ScalarValue -> itemMutator.updateValue(item, attributeValue)
            is DictionaryValue -> itemMutator.updateValue(item, attributeValue)
            else -> throw RuntimeException("Unknown value type: ${attributeValue.javaClass.name}.")
        }
    }
}