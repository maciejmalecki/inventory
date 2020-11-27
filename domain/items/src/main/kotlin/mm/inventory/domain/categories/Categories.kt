package mm.inventory.domain.categories

import kotlinx.collections.immutable.ImmutableList

data class CategoryCompound(val code: String, val name: String)
data class CategoryPath(val compounds: ImmutableList<CategoryCompound>)
