package mm.inventory.infra.store.inventory.stock

import mm.inventory.app.productplanner.item.asAppId
import mm.inventory.app.productplanner.stock.ItemStockAppId
import mm.inventory.app.productplanner.stock.asAppId
import mm.inventory.domain.inventory.stock.DeductCommand
import mm.inventory.domain.inventory.stock.ItemStock
import mm.inventory.domain.inventory.stock.ItemStockRepository
import mm.inventory.domain.inventory.stock.MutableItemStock
import mm.inventory.domain.inventory.stock.ReplenishCommand
import mm.inventory.domain.shared.InvalidDataException
import mm.inventory.domain.shared.types.ItemId
import mm.inventory.infra.store.updateAndExpect
import org.jdbi.v3.core.Jdbi
import java.math.BigDecimal

class ItemStockJdbiRepository(private val db: Jdbi) : ItemStockRepository {

    override fun findByItemId(itemId: ItemId): ItemStock = db.withHandle<ItemStock, RuntimeException> { handle ->
        val dao = handle.attach(ItemStockDao::class.java)
        val itemStock = dao.selectStockAmount(itemId.asAppId()) ?: ItemStockRec(BigDecimal.ZERO, 0)
        return@withHandle ItemStock(id = ItemStockAppId(itemId.asAppId(), itemStock.serial), amount = itemStock.amount)
    }

    override fun update(itemStock: MutableItemStock): Unit = db.useTransaction<RuntimeException> { handle ->
        val dao = handle.attach(ItemStockDao::class.java)
        itemStock.consume { command ->
            // TODO this will fail if more than one command is executed at once because of the "optimistic" locking
            when(command) {
                is ReplenishCommand -> updateAndExpect(1) {
                    dao.insertItemStock(command.base.id.asAppId(), command.amount)
                }
                is DeductCommand -> updateAndExpect(1) {
                    // TODO check for negative value
                    dao.insertItemStock(command.base.id.asAppId(), -command.amount)
                }
                else -> throw InvalidDataException("Unsupported command ${command.javaClass.name}.")
            }
        }
    }
}