package mm.inventory.adapters.store.jdbi.items

import mm.inventory.domain.shared.types.ItemId

fun createItemId(id: String): ItemId = JdbiItemId(id)

internal class JdbiItemId(val id: String) : ItemId {
    override fun equals(other: Any?): Boolean =
        when (other) {
            null -> false
            is JdbiItemId -> other.toJdbiId()?.id == id
            else -> false
        }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

internal fun ItemId.toJdbiId(): JdbiItemId? =
    when (this) {
        is JdbiItemId -> this
        else -> null
    }

internal fun ItemId.asJdbiId(): JdbiItemId =
    this.toJdbiId() ?: throw IllegalArgumentException("Unsupported type ${this.javaClass}.")
