package mm.inventory.app.categories.import

import kotlinx.collections.immutable.ImmutableList
import mm.inventory.app.categories.CategoryCrudRepository
import mm.inventory.app.categories.CategorySection

const val CATEGORY_SEPARATOR = "-"

/**
 * Import category tree from external source.
 */
class CategoryImporter(private val categoryCrudRepository: CategoryCrudRepository) {

    private data class PathFound(val categoryId: Long, val length: Int)

    private lateinit var categories: MutableMap<String, Long>

    suspend fun import(category: ImmutableList<CategorySection>) {
        if (!this::categories.isInitialized) {
            categories = loadCategories()
        }
        var (parentCategoryId, foundPos) = findPath(categories, category, category.size)
        if (foundPos == 0) {
            parentCategoryId = categoryCrudRepository.create(category[0].code, category[0].name).id
            val path = category[0].code
            categories[path] = parentCategoryId
            foundPos++
        }
        for (pos: Int in foundPos until category.size) {
            parentCategoryId = categoryCrudRepository.create(category[pos].code, category[pos].name, parentCategoryId).id
            val path = category.slice(0..pos).map { it.code }.joinToString(CATEGORY_SEPARATOR)
            categories[path] = parentCategoryId
        }
    }

    private tailrec fun findPath(categories: Map<String, Long>, category: ImmutableList<CategorySection>, pos: Int): PathFound {
        if (pos == 0) {
            return PathFound(-1, 0)
        }
        val categoryStr = category
                .slice(0 until pos)
                .map {
                    it.code
                }
                .joinToString(CATEGORY_SEPARATOR)
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
    private suspend fun loadCategories(): MutableMap<String, Long> {
        val paths = categoryCrudRepository.findAllPathNames(CATEGORY_SEPARATOR)
        val result = HashMap<String, Long>()
        for (path in paths) {
            result[path.path] = path.leafId
        }
        return result
    }
}