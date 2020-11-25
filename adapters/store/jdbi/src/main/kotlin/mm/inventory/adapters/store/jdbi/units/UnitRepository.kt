package mm.inventory.adapters.store.jdbi.units

import org.jdbi.v3.sqlobject.statement.SqlQuery

interface UnitRepository {

    @SqlQuery("SELECT code, name FROM Units")
    fun getAllUnits(): List<UnitRec>
}