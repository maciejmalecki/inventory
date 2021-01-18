package mm.inventory.app.productplanner.item

import mm.inventory.domain.shared.types.ItemId

data class ItemAppId(val id: String) : ItemId

fun ItemId.toAppId(): ItemAppId? =
    when (this) {
        is ItemAppId -> this
        else -> null
    }

fun ItemId.asAppId(): ItemAppId =
    this.toAppId() ?: throw IllegalArgumentException("Unknown implementation: ${this.javaClass.name}.")
