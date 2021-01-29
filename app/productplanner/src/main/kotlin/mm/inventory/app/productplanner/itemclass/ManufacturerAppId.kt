package mm.inventory.app.productplanner.itemclass

import mm.inventory.domain.shared.types.ManufacturerId

data class ManufacturerAppId(val id: Long) : ManufacturerId

fun ManufacturerId.toAppId(): ManufacturerAppId? =
    when (this) {
        is ManufacturerAppId -> this
        else -> null
    }

fun ManufacturerId.asAppId(): ManufacturerAppId =
    this.toAppId() ?: throw IllegalArgumentException("Unknown implementation: ${this.javaClass.name}.")
