package mm.inventory.adapters.store.jdbi.units

import org.jdbi.v3.sqlobject.statement.SqlQuery

interface UnitDao {

    @SqlQuery("SELECT code, name FROM Units")
    fun findAll(): List<UnitRec>

    @SqlQuery("SELECT code, name FROM Units WHERE code=?")
    fun findByCode(code: String): UnitRec?
}

data class UnitRec(val code: String, val name: String)