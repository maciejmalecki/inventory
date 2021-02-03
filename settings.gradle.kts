rootProject.name = "inventory"

val skipFrontend: Boolean = startParameter.systemPropertiesArgs["skipFrontend"]?.toBoolean() ?:false

// -- domain circle
include(":domain:shared")
include(":domain:items")
include(":domain:inventory")
include(":domain:production")

// -- application circle
include(":app:product-planner")
include(":app:categories")

// -- infrastructure circle

// store implementations
// ...R2DBC
include(":infra:store:categories-store")
// ...JDBI
include(":infra:store:jdbi-common")
include(":infra:store:items-store")
include(":infra:store:inventory-store")

// web app implementations
include(":infra:web:product-planner-backend")
include(":infra:web:categories-backend")
if(!skipFrontend) {
    include(":infra:client:product-planner-ng")
}
