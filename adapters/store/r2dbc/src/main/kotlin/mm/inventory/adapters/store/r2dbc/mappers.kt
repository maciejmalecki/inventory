package mm.inventory.adapters.store.r2dbc

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import mm.inventory.app.categories.Category

fun categoryMapper(row: Row, rowMetadata: RowMetadata) = Category(
        row.get("category_id", Long::class.java)!!,
        row.get("code", String::class.java)!!,
        row.get("name", String::class.java)!!)