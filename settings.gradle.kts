rootProject.name = "inventory"

// -- domain circle
include(":domain:shared")
include(":domain:items")
include(":domain:inventory")
include(":domain:production")

// -- application circle
include(":app:productplanner")
include(":app:categories")
include(":app:importcategories")

// -- infrastructure circle

// store implementations
include(":infra:store:sql")
include(":infra:store:r2dbc")
include(":infra:store:jdbi")

// web app implementations
include(":infra:web:spring")
include(":infra:web:webflux")
