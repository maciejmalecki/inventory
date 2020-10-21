plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":domain:itemclasses"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}