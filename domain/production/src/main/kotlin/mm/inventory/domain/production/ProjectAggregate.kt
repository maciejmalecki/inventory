package mm.inventory.domain.production

import kotlinx.collections.immutable.ImmutableSet
import java.math.BigDecimal
import java.time.LocalDateTime

data class Project(
    val projectCode: String,
    val description: String,
    val revisions: ImmutableSet<ProjectRevision>
)

data class ProjectRevision(
    val revisionCode: String,
    val createdAt: LocalDateTime,
    val itemUsages: ImmutableSet<Usage>
)

data class Usage(
    val itemCode: String,
    val amount: BigDecimal
)
