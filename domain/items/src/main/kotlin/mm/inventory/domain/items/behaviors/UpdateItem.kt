package mm.inventory.domain.items.behaviors

import kotlinx.collections.immutable.toImmutableSet
import mm.inventory.domain.items.item.Item
import mm.inventory.domain.items.item.ItemMutator
import mm.inventory.domain.items.item.ItemSelector
import mm.inventory.domain.items.item.parse
import mm.inventory.domain.items.itemclass.ItemClassSelector
import mm.inventory.domain.shared.transactions.BusinessTransaction
import mm.inventory.domain.shared.types.ItemId

class UpdateItem(
    private val tx: BusinessTransaction,
    private val itemSelector: ItemSelector,
    private val itemMutator: ItemMutator,
    private val itemClassSelector: ItemClassSelector
) {

    fun execute(id: ItemId, inValues: Map<String, String>): Item =
        tx.inTransaction {
            val item = itemSelector.get(id)
            val itemClass = itemClassSelector.get(item.itemClassId)
            val values = inValues.entries.map {
                itemClass.getAttribute(it.key).parse(it.value)
            }.toImmutableSet()

            return@inTransaction itemMutator.save(item.updateValues(values))
        }
}