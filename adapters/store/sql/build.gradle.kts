plugins {
    kotlin("jvm")
    id("org.liquibase.gradle") version "2.0.4"
}

val collectionsImmutableVersion: String by project
val r2dbcClientVersion: String by project
val kotlinxCoroutinesVersion: String by project

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}