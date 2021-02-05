package mm.inventory.infra.store.jdbi.itemclasses

import mm.inventory.app.productplanner.itemclass.ItemClassAppId
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface ProposedCategoriesDao {

    @SqlQuery("SELECT category_id FROM Proposed_Categories WHERE name=:id.id AND version=:id.version")
    fun selectForItemClass(id: ItemClassAppId): List<Long>
}