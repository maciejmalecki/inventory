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
import mm.inventory.domain.shared.mutations.MutatingCommand
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
        itemStock.consume { command: MutatingCommand<ItemStock>, itemStockAppId: ItemStockAppId? ->
            // we use it to increment serial (optimistic lock)
            val id = itemStockAppId ?: command.base.id.asAppId()
            when (command) {
                is ReplenishCommand -> {
                    dao.insertItemStock(command.base.id.asAppId(), command.amount)
                    id.copy(serial = id.serial + 1)
                }

                is DeductCommand -> {
                    dao.insertItemStock(command.base.id.asAppId(), -command.amount)
                    id.copy(serial = id.serial + 1)
                }
                else -> throw InvalidDataException("Unsupported command ${command.javaClass.name}.")
            }
        }
    }
}