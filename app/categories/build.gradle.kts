plugins {
    kotlin("jvm")
}

val collectionsImmutableVersion: String by project
val kotlinxCoroutinesVersion: String by project

dependencies {
    implementation(project(":domain:itemclasses"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")
}