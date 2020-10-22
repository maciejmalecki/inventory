plugins {
    kotlin("jvm") version "1.4.10"
}

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
}

allprojects {
    group = "gh.mm"
    version = "1.0.0-SNAPSHOT"



    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_14.toString()
        }
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }

    repositories {
        maven("https://repo.spring.io/milestone")
        mavenCentral()
        jcenter()
    }
}