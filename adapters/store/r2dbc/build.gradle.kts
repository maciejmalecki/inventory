plugins {
    kotlin("jvm")
}

val collectionsImmutableVersion: String by project
val r2dbcClientVersion: String by project
val kotlinxCoroutinesVersion: String by project

dependencies {
    implementation(project(":domain:itemclasses"))
    implementation(project(":app:categories"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")
    implementation("io.r2dbc:r2dbc-client:$r2dbcClientVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxCoroutinesVersion")
}