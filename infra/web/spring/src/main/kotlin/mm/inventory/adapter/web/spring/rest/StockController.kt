package mm.inventory.adapter.web.spring.rest

import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.app.productplanner.stock.StockFacade
import mm.inventory.domain.inventory.stock.ItemStock
import mm.inventory.domain.shared.InvalidDataException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

data class ChangeStockRequest(val replenish: BigDecimal?, val deduct: BigDecimal?)

@RestController
class StockController(private val stockFacade: StockFacade) {

    @GetMapping("/stock/{id}")
    fun stock(@PathVariable id: String): ResponseEntity<ItemStock> =
        ResponseEntity.ok(stockFacade.findByItemId(ItemAppId((id))))

    @PostMapping("/stock/{id}")
    fun changeStock(
        @PathVariable id: String,
        @RequestBody changeRequest: ChangeStockRequest
    ): ResponseEntity<ItemStock> {
        val itemId = ItemAppId(id)
        when {
            changeRequest.replenish != null -> stockFacade.replenish(itemId, changeRequest.replenish)
            changeRequest.deduct != null -> stockFacade.deduct(itemId, changeRequest.deduct)
            else -> throw InvalidDataException("Both replenish and deduct are null.")
        }
        return ResponseEntity.ok(stockFacade.findByItemId(itemId))
    }
}