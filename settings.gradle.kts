rootProject.name = "inventory"

include(":domain:itemclasses")
include(":domain:items")

include(":app:categories")

include(":adapters:store:r2dbc")
include(":adapters:web:spring")
