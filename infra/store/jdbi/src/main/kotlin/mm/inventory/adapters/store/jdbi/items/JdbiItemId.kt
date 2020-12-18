package mm.inventory.adapters.store.jdbi.items

import mm.inventory.domain.shared.types.ItemId

fun createItemId(id: String): ItemId = JdbiItemId(id)

internal class JdbiItemId(internal val id: String) : ItemId {
    override fun `==`(other: ItemId): Boolean {
        val jdbiId = other.toJdbiId() ?: return false
        return jdbiId.id == id
    }

    override fun toString() = id
}

internal fun ItemId.toJdbiId(): JdbiItemId? =
    when (this) {
        is JdbiItemId -> this
        else -> null
    }

internal fun ItemId.asJdbiId(): JdbiItemId =
    this.toJdbiId() ?: throw IllegalArgumentException("Unsupported type ${this.javaClass}.")
