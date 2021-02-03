plugins {
    id("org.springframework.boot") version "2.3.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm")
    kotlin("plugin.spring") version "1.3.72"
}

val collectionsImmutableVersion: String by project
val r2dbcPostgresqlVersion: String by project
val r2dbcClientVersion: String by project
val kotlinxCoroutinesVersion: String by project
val postgresqlJdbcVersion: String by project

dependencies {
    implementation(project(":app:categories"))
    implementation(project(":infra:store:categories-store"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")
    implementation("io.r2dbc:r2dbc-client:$r2dbcClientVersion")
    implementation("io.r2dbc:r2dbc-postgresql:$r2dbcPostgresqlVersion")
    implementation("org.postgresql:postgresql:$postgresqlJdbcVersion")
}
