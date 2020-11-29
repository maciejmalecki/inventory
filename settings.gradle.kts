rootProject.name = "inventory"

include(":domain:items")
include(":domain:inventory")
include(":domain:production")

include(":app:categories")
include(":app:importcategories")

include(":adapters:store:sql")
include(":adapters:store:r2dbc")
include(":adapters:store:jdbi")
include(":adapters:web:spring")
