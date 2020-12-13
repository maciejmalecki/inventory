plugins {
    kotlin("jvm")
    id("com.diffplug.spotless") version "5.8.2"
}

// TODO: not working with Kotlin 1.4
//configure<com.diffplug.gradle.spotless.SpotlessExtension> {
//    kotlin {
//        ktlint()
//        ktfmt()
//        prettier()
//    }
//}

val collectionsImmutableVersion: String by project

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")
}