package mm.inventory.app.productplanner.itemclass

import mm.inventory.domain.shared.types.ItemClassId

data class ItemClassAppId(val id: String, val version: Long = -1L) : ItemClassId {
    val useNewest: Boolean = version == -1L
}

fun ItemClassId.toAppId(): ItemClassAppId? =
    when (this) {
        is ItemClassAppId -> this
        else -> null
    }

fun ItemClassId.asAppId(): ItemClassAppId =
    this.toAppId() ?: throw IllegalArgumentException("Unknown implementation: ${this.javaClass.name}.")
