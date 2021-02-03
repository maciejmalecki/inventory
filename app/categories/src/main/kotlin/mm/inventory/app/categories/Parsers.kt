package mm.inventory.app.categories.import

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import mm.inventory.app.categories.CategorySection

/**
 * Simple category parser for string representation. Code and name is assumed to be the same.
 * @param line string data to be parsed
 * @param separator optional separator for string representation. By default it is '/'.
 * @return list of parsed category sections
 */
fun simpleParser(line: String, separator: String = "/"): ImmutableList<CategorySection> =
        line.split(separator).map {
            CategorySection(it, it)
        }.toImmutableList()