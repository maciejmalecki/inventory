package mm.inventory.app.productplanner

import mm.inventory.domain.shared.security.Role

val ITEMS_ROLE = object : Role("items") {}
val ITEMS_WRITER_ROLE = object : Role("items/writer") {}
val ITEM_CLASSES_ROLE = object : Role("itemClasses") {}
val ITEM_CLASSES_WRITER_ROLE = object : Role("itemClasses/writer") {}
val STOCK_ROLE = object : Role("stock") {}
val STOCK_WRITER_ROLE = object : Role("stock/writer") {}