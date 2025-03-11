plugins {
    val kotlinVersion = "2.1.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    val springBootVersion = "3.4.3"
    id("org.springframework.boot") version springBootVersion

    `java-test-fixtures`
}

apply(plugin = "io.spring.dependency-management")

group = "me.jimmyberg.ams"
version = "0.0.1"

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

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring3x:4.14.0")

    // kotest
    val kotestVersion = "5.9.0"
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")

    // fixture-monkey
    val fixtureMonkeyVersion = "1.1.10"
    testFixturesImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:$fixtureMonkeyVersion")
//    testFixturesImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:$fixtureMonkeyVersion")
//    testFixturesImplementation("com.navercorp.fixturemonkey:fixture-monkey-kotest:$fixtureMonkeyVersion")
//    testFixturesImplementation("com.navercorp.fixturemonkey:fixture-monkey-jackson:$fixtureMonkeyVersion")
//    testFixturesImplementation("com.navercorp.fixturemonkey:fixture-monkey-jakarta-validation:$fixtureMonkeyVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
