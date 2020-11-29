plugins {
    kotlin("jvm")
}

val collectionsImmutableVersion: String by project

dependencies {
    implementation(project(":domain:items"))
    implementation(project(":domain:inventory"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")
}
