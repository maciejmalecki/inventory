package mm.inventory.domain.production

import kotlinx.collections.immutable.ImmutableSet
import mm.inventory.domain.items.Item
import java.math.BigDecimal

data class ProductionPlanItems(
    val item: Item,
    val amount: BigDecimal
)

data class ProductionPlan (
        val items: ImmutableSet<ProductionPlanItems>
)