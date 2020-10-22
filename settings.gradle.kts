rootProject.name = "inventory"

include(":domain:itemclasses")
include(":domain:items")

include(":adapters:store:sql")
include(":adapters:web:spring")
