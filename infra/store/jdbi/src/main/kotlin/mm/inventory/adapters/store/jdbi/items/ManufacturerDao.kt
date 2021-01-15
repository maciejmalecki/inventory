package mm.inventory.adapters.store.jdbi.items

import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface ManufacturerDao {
    @SqlQuery("SELECT id, name FROM Manufacturers ORDER BY name")
    fun selectAll(): List<ManufacturerRec>

    @SqlUpdate("INSERT INTO Manufacturers (name) VALUES (:manufacturer.name)")
    @GetGeneratedKeys("id")
    fun insert(manufacturer: ManufacturerRec): Long
}

data class ManufacturerRec(val id: Long, val name: String)
