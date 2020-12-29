plugins {
    kotlin("jvm")
}

val collectionsImmutableVersion: String by project
val vavrKotlinVersion: String by project

dependencies {
    implementation(project(":domain:shared"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")
    implementation("io.vavr:vavr-kotlin:$vavrKotlinVersion")
}