rootProject.name = "inventory"

include(":domain:itemclasses")
include(":domain:items")

include(":app:categories")
include(":app:importcategories")

include(":adapters:store:r2dbc")
include(":adapters:web:spring")
