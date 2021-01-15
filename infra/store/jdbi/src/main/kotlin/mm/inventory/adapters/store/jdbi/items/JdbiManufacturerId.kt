package mm.inventory.adapters.store.jdbi.items

import mm.inventory.domain.shared.types.ItemId
import mm.inventory.domain.shared.types.ManufacturerId

fun createManufacturerId(id: Long): ManufacturerId = JdbiManufacturerId(id)

internal class JdbiManufacturerId(val id: Long) : ManufacturerId {
    override fun equals(other: Any?): Boolean =
        when (other) {
            null -> false
            is JdbiManufacturerId -> other.id == id
            else -> false
        }

    override fun hashCode(): Int = id.hashCode()
}

internal fun ManufacturerId.toJdbiId(): JdbiManufacturerId? =
    when (this) {
        is JdbiManufacturerId -> this
        else -> null
    }

internal fun ManufacturerId.asJdbiId(): JdbiManufacturerId =
    this.toJdbiId() ?: throw IllegalArgumentException("Unsupported type ${this.javaClass}.")