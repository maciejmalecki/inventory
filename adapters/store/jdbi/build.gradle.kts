plugins {
    kotlin("jvm")
}

val collectionsImmutableVersion: String by project
val jdbiVersion: String by project

dependencies {
    implementation(project(":domain:transactions"))
    implementation(project(":domain:items"))
    implementation(project(":domain:inventory"))
    implementation(project(":domain:production"))

    implementation(project(":app:itemsfacade"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")

    implementation(platform("org.jdbi:jdbi3-bom:$jdbiVersion"))
    implementation("org.jdbi:jdbi3-core")
    implementation("org.jdbi:jdbi3-sqlobject")
    implementation("org.jdbi:jdbi3-kotlin")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject")
}