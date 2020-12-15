package mm.inventory.domain.production

import java.math.BigDecimal

interface BookingRepository {
    fun bookLine(productionRunId: String, itemCode: String, bigDecimal: BigDecimal): LineBooking
    fun deduct(booking: LineBooking): BigDecimal
}