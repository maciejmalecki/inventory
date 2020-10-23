plugins {
    id("org.springframework.boot") version "2.3.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm")
    kotlin("plugin.spring") version "1.3.72"
}

val collectionsImmutableVersion: String by project
val r2dbcVersion: String by project

dependencies {
    implementation(project(":domain:itemclasses"))
    implementation(project(":adapters:store:sql"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")
    implementation("io.r2dbc:r2dbc-client:0.8.0.RC1")

    implementation("io.r2dbc:r2dbc-postgresql:$r2dbcVersion")

    runtimeOnly("org.postgresql:postgresql")
}
