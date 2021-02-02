plugins {
    id("org.springframework.boot") version "2.3.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm")
    kotlin("plugin.spring") version "1.3.72"
}

val collectionsImmutableVersion: String by project
val vavrKotlinVersion: String by project
val jdbiVersion: String by project
val postgresqlJdbcVersion: String by project

dependencies {
    implementation(project(":domain:shared"))
    implementation(project(":domain:items"))
    implementation(project(":domain:inventory"))

    implementation(project(":app:productplanner"))

    implementation(project(":infra:store:jdbi-common"))
    implementation(project(":infra:store:items-store"))
    implementation(project(":infra:store:inventory-store"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsImmutableVersion")
    implementation("io.vavr:vavr-kotlin:$vavrKotlinVersion")

    implementation("org.postgresql:postgresql:$postgresqlJdbcVersion")

    implementation(platform("org.jdbi:jdbi3-bom:$jdbiVersion"))
    implementation("org.jdbi:jdbi3-core")
    implementation("org.jdbi:jdbi3-sqlobject")
    implementation("org.jdbi:jdbi3-spring4")
    implementation("org.jdbi:jdbi3-postgres")
    implementation("org.jdbi:jdbi3-kotlin")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject")
    implementation("org.jdbi:jdbi3-stringtemplate4")
}
