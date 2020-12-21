package mm.inventory.domain.items.itemclass

/**
 * Unit of measurement.
 * @param code SI symbol (i.e.: s, m, kg)
 * @param name SI base unit name (i.e.: second, meter, kilogram)
 */
data class UnitOfMeasurement(val code: String, val name: String)