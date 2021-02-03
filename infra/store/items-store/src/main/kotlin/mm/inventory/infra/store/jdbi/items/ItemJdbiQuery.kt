package mm.inventory.infra.store.jdbi.items

import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.app.productplanner.item.ItemHeader
import mm.inventory.app.productplanner.item.ItemQuery
import mm.inventory.app.productplanner.item.ItemSearchCriteria
import org.jdbi.v3.core.Jdbi

data class ItemSearchJdbiCriteria(
    val name: String?,
    val manufacturersCode: String?
)

private fun like(value: String?) = value?.let { "%$value%" }

internal fun ItemSearchCriteria.toJdbiCriteria() =
    ItemSearchJdbiCriteria(like(name), like(manufacturersCode))

class ItemJdbiQuery(private val db: Jdbi) : ItemQuery {

    override fun findAll(): List<ItemHeader> =
        db.withHandle<List<ItemHeader>, RuntimeException> { handle ->
            handle.attach(ItemDao::class.java).selectItems().map { itemRec ->
                ItemHeader(ItemAppId(itemRec.name), itemRec.name, itemRec.itemClassName, itemRec.manufacturersCode)
            }
        }

    override fun findByCriteria(criteria: ItemSearchCriteria): List<ItemHeader> =
        db.withHandle<List<ItemHeader>, RuntimeException> { handle ->
            val dao = handle.attach(ItemDao::class.java)
            dao.selectItemsByCriteria(
                criteria.toJdbiCriteria(),
                criteria.manufacturerIds?.map { it.id },
                criteria.itemClassIds?.map { it.id }
            ).map {
                ItemHeader(
                    id = ItemAppId(it.name),
                    name = it.name,
                    itemClassName = it.itemClassName,
                    manufacturersCode = it.manufacturersCode
                )
            }
        }
}