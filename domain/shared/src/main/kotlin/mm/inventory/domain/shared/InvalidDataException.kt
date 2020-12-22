package mm.inventory.domain.shared

/**
 * Business exception denoting that provided data is invalid.
 */
class InvalidDataException(msg: String) : RuntimeException(msg)