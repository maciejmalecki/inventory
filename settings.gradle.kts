rootProject.name = "inventory"

// -- domain circle
include(":domain:shared")
include(":domain:items")
include(":domain:inventory")
include(":domain:production")

// -- application circle
include(":app:itemsfacade")
include(":app:productionfacade")
include(":app:categories")
include(":app:importcategories")

// -- infrastructure circle

// store implementations
include(":adapters:store:sql")
include(":adapters:store:r2dbc")
include(":adapters:store:jdbi")

// web app implementations
include(":adapters:web:spring")
include(":adapters:web:webflux")
