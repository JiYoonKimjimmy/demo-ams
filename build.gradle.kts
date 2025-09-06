import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    val kotlinVersion = "2.1.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    val springBootVersion = "3.4.4"
    id("org.springframework.boot") version springBootVersion
    id("org.asciidoctor.jvm.convert") version "3.3.2"

    `java-test-fixtures`
}

apply(plugin = "io.spring.dependency-management")

group = "me.jimmyberg.ams"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

val snippetsDir: File by extra { file("build/generated-snippets") }

val asciidoctorExt: Configuration by configurations.creating

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    runtimeOnly("com.h2database:h2")

    // exposed
    implementation(libs.exposed.spring.boot.starter)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.kotlin.datetime)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation(libs.mockk)

    // kotest
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.extensions.spring)

    // fixture-monkey
    testFixturesImplementation(libs.fixture.monkey.starter.kotlin)
    testFixturesImplementation(libs.fixture.monkey.kotest)
    testFixturesImplementation(libs.fixture.monkey.jackson)
    testFixturesImplementation(libs.fixture.monkey.jakarta.validation)

    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    outputs.dir(snippetsDir)
    finalizedBy(tasks.named("asciidoctor"))
}

tasks.withType<AsciidoctorTask> {
    inputs.dir(snippetsDir)
    configurations(asciidoctorExt.name)
    dependsOn(tasks.withType<Test>())
}

val asciidoctorTask = tasks.named<AsciidoctorTask>("asciidoctor")

tasks.withType<BootJar> {
    dependsOn(asciidoctorTask)
    from(asciidoctorTask.get().outputDir) {
        into("static/docs")
    }
}
