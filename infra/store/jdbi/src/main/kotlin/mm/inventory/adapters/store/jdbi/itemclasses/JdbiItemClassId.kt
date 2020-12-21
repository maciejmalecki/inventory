package mm.inventory.adapters.store.jdbi.itemclasses

import mm.inventory.domain.shared.types.ItemClassId

fun createItemClassId(id: String): ItemClassId = JdbiItemClassId(id)

internal data class JdbiItemClassId(val id: String) : ItemClassId {
    override fun `==`(other: ItemClassId): Boolean {
        val jdbiId = other.toJdbiId() ?: return false
        return jdbiId.id == id
    }

    override fun toString() = id
}

internal fun ItemClassId.toJdbiId(): JdbiItemClassId? =
    when (this) {
        is JdbiItemClassId -> this
        else -> null
    }

internal fun ItemClassId.asJdbiId(): JdbiItemClassId =
    this.toJdbiId() ?: throw IllegalArgumentException("Unknown implementaiton: ${this.javaClass.name}.")


