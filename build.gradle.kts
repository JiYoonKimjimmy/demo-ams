import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
}

group = "me.jimmyberg.ams"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test") { exclude(module = "mockito-core") }
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.12.6")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
