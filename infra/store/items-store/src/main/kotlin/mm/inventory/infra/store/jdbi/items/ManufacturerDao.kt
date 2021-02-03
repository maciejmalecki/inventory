package mm.inventory.infra.store.jdbi.items

import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface ManufacturerDao {
    @SqlQuery("SELECT id, name FROM Manufacturers ORDER BY name")
    fun selectAll(): List<ManufacturerRec>

    @SqlQuery("SELECT id, name FROM Manufacturers WHERE id=?")
    fun selectById(id: Long): ManufacturerRec?

    @SqlUpdate("INSERT INTO Manufacturers (name) VALUES (:name)")
    @GetGeneratedKeys("id")
    fun insert(@BindBean manufacturer: ManufacturerRec): Long
}

data class ManufacturerRec(val id: Long, val name: String)
