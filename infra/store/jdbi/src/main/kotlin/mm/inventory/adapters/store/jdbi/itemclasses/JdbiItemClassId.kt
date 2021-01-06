package mm.inventory.adapters.store.jdbi.itemclasses

import mm.inventory.domain.shared.types.ItemClassId

fun createItemClassId(id: String, version: Long = -1L): ItemClassId = JdbiItemClassId(id, version)

internal data class JdbiItemClassId(val id: String, val version: Long) : ItemClassId {
    val useNewest: Boolean = version == -1L
}

internal fun ItemClassId.toJdbiId(): JdbiItemClassId? =
    when (this) {
        is JdbiItemClassId -> this
        else -> null
    }

internal fun ItemClassId.asJdbiId(): JdbiItemClassId =
    this.toJdbiId() ?: throw IllegalArgumentException("Unknown implementation: ${this.javaClass.name}.")
