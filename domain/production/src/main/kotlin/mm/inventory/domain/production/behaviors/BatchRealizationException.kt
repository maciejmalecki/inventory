package mm.inventory.domain.production.behaviors

/**
 * Business exception stating, that the production batch realization was not possible.
 */
class BatchRealizationException(msg: String) : RuntimeException(msg)