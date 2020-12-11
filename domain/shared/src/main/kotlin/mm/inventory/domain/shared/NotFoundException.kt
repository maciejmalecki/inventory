package mm.inventory.domain.shared

/**
 * Business exception denoting that given object cannot be localized / found / loaded.
 */
class NotFoundException(msg: String) : RuntimeException(msg)