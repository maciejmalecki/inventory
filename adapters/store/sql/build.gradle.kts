plugins {
    kotlin("jvm")
    id("org.liquibase.gradle") version "2.0.4"
}

val collectionsImmutableVersion: String by project

dependencies {
    implementation(project(":domain:itemclasses"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")
    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")

}