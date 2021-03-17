plugins {
    kotlin("jvm")
}

val collectionsImmutableVersion: String by project
val archUnitVersion: String by project
val junitPlatformVersion: String by project

dependencies {
    implementation(project(":domain:shared"))
    implementation(project(":domain:items"))
    implementation(project(":domain:inventory"))
    implementation(project(":domain:production"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")

    testImplementation("com.tngtech.archunit:archunit-junit5:$archUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitPlatformVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitPlatformVersion")
}