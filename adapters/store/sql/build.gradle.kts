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
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.r2dbc:r2dbc-client:0.8.0.RC1")
}