package mm.inventory.app.productplanner.stock

import mm.inventory.app.productplanner.STOCK_ROLE
import mm.inventory.app.productplanner.STOCK_WRITER_ROLE
import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.domain.inventory.stock.ItemStock
import mm.inventory.domain.inventory.stock.ItemStockRepository
import mm.inventory.domain.shared.security.SecurityGuard
import mm.inventory.domain.shared.transactions.BusinessTransaction
import java.math.BigDecimal

class StockFacade(
    private val sec: SecurityGuard,
    private val tx: BusinessTransaction,
    private val itemStockRepository: ItemStockRepository,
    private val itemStockQuery: ItemStockQuery
) {

    fun findByItemId(itemId: ItemAppId): ItemStock = sec.requireRole(STOCK_ROLE) {
        itemStockRepository.findByItemId(itemId)
    }

    fun findByItemIds(ids: List<ItemAppId>): List<ItemStock> = sec.requireRole(STOCK_ROLE) {
        itemStockRepository.findByItemIds(ids)
    }

    fun findAllStock(): List<ItemStockHeader> = sec.requireRole(STOCK_ROLE) {
        itemStockQuery.findAllStock()
    }

    fun replenish(itemId: ItemAppId, amount: BigDecimal) = sec.requireAllRoles(STOCK_ROLE, STOCK_WRITER_ROLE) {
        tx.inTransaction {
            val itemStock = itemStockRepository.findByItemId(itemId).mutable()
            itemStock.replenish(amount)
            itemStockRepository.update(itemStock)
        }
    }

    fun deduct(itemId: ItemAppId, amount: BigDecimal) = sec.requireAllRoles(STOCK_ROLE, STOCK_WRITER_ROLE) {
        tx.inTransaction {
            val itemStock = itemStockRepository.findByItemId(itemId).mutable()
            itemStock.deduct(amount)
            itemStockRepository.update(itemStock)
        }
    }
}
