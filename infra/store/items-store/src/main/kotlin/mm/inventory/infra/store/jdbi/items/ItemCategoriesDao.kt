package mm.inventory.infra.store.jdbi.items

import mm.inventory.app.productplanner.item.ItemAppId
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface ItemCategoriesDao {

    @SqlQuery("SELECT category_id FROM Item_Categories WHERE item_name=:id.id")
    fun selectForItem(id: ItemAppId): List<Long>
}