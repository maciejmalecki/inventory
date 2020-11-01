package mm.inventory.app.categories.import

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.app.categories.CategoryCrudRepository
import mm.inventory.app.categories.CategorySection

val CATEGORY_SEPARATOR = "-"

/**
 * Import category tree from external source.
 */
class CategoryImporter(private val categoryCrudRepository: CategoryCrudRepository) {

    private data class PathFound(val categoryId: Long, val length: Int)

    private lateinit var categories: Map<String, Long>

    suspend fun import(category: ImmutableList<CategorySection>) {
        if (!this::categories.isInitialized) {
            categories = loadCategories()
        }
        var (parentCategoryId, foundPos) = findPath(categories, category, category.size)
        if (foundPos == 0) {
            parentCategoryId = categoryCrudRepository.create(category[foundPos].code, category[foundPos].name).id
        }
        for (pos: Int in foundPos + 1 until category.size) {
            parentCategoryId = categoryCrudRepository.create(category[pos].code, category[pos].name, parentCategoryId).id
        }
    }

    private tailrec fun findPath(categories: Map<String, Long>, category: ImmutableList<CategorySection>, pos: Int): PathFound {
        if (pos == 0) {
            return PathFound(-1, 0)
        }
        val categoryStr = category.slice(0..pos).joinToString(CATEGORY_SEPARATOR)
        val categoryId = categories[categoryStr]
        if (categoryId != null) {
            return PathFound(categoryId, pos)
        }
        return findPath(categories, category, pos - 1)
    }

    /**
     * Lazy load of category tree. This is a workaround to use suspend function as the code is reactive. It is not
     * possible to call it from constructor (yet).
     */
    private suspend fun loadCategories() =
            categoryCrudRepository.findAllPathNames(CATEGORY_SEPARATOR)
                    .fold(HashMap<String, Long>()) { acc, categoryPath ->
                        acc[categoryPath.path] = categoryPath.leafId
                        return acc
                    }
}