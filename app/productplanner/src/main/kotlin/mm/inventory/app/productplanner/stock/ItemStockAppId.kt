package mm.inventory.app.productplanner.stock

import mm.inventory.app.productplanner.item.ItemAppId
import mm.inventory.domain.shared.types.ItemStockId

data class ItemStockAppId(override val itemId: ItemAppId, val serial: Int): ItemStockId

fun ItemStockId.toAppId(): ItemStockAppId? =
    when (this) {
        is ItemStockAppId -> this
        else -> null
    }

fun ItemStockId.asAppId(): ItemStockAppId =
    this.toAppId() ?: throw IllegalArgumentException("Unknown implementation: ${this.javaClass.name}.")
