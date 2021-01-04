package mm.inventory.adapters.store.jdbi.itemclasses

import mm.inventory.domain.shared.types.ItemClassId

fun createItemClassId(id: String): ItemClassId = JdbiItemClassId(id)

internal data class JdbiItemClassId(val id: String) : ItemClassId {
    override fun equals(other: Any?): Boolean =
        when (other) {
            null -> false
            is JdbiItemClassId -> other.toJdbiId()?.id == id
            else -> false
        }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

internal fun ItemClassId.toJdbiId(): JdbiItemClassId? =
    when (this) {
        is JdbiItemClassId -> this
        else -> null
    }

internal fun ItemClassId.asJdbiId(): JdbiItemClassId =
    this.toJdbiId() ?: throw IllegalArgumentException("Unknown implementation: ${this.javaClass.name}.")
