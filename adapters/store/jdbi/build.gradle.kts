plugins {
    kotlin("jvm")
}

val collectionsImmutableVersion: String by project
val jdbiVersion: String by project
val postgresqlJdbcVersion: String by project

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")
    implementation(platform("org.jdbi:jdbi-bom:$jdbiVersion"))
    implementation("org.postgresql:postgresql:$postgresqlJdbcVersion")
}