import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.graalvm.buildtools.native") version "0.10.2"
    kotlin("jvm") version "1.9.24"
    id("org.jetbrains.kotlin.plugin.spring") version "2.0.0"
}

group = "io.acme"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // Arrow to have an excuse for standing here
    val arrowVersion = "1.2.4"
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-fx-coroutines:$arrowVersion")
    val kotestArrowVersion = "1.4.0"
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:$kotestArrowVersion")

    // Spring because most of you hopefully know it
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    val kotestSpringVersion = "1.3.0"
    testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestSpringVersion")
    //We love JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    val kotestJsonVersion = "5.9.1"
    implementation("io.kotest:kotest-assertions-json:$kotestJsonVersion")

    //implementation("org.jetbrains.kotlin:kotlin-reflect")


    // Kotest because we want tests to be nice to read and write
    val kotestVersion = "5.9.0"
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

    // Mocking is rocking
    val mockkVersion = "1.13.11"
    testImplementation("io.mockk:mockk:${mockkVersion}")


    // Wiremock for meaningful integration tests
    val wireMockVersion = "3.1.0"
    testImplementation("io.kotest.extensions:kotest-extensions-wiremock:$wireMockVersion")


    // Kotlinx datetime to not rage quit time handling
    val dateTimeVersion = "0.6.0"
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    val kotestDateTimeVersion = "4.4.3"
    testImplementation("io.kotest:kotest-assertions-kotlinx-time:$kotestDateTimeVersion")

    // H2 for JPA tests
    runtimeOnly("com.h2database:h2")

}


kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget = JvmTarget.JVM_21
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
