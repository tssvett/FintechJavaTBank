/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.10/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    jacoco
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}


dependencies {
    implementation(project(":logtime"))

    testImplementation(libs.junit.jupiter)

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.4")

    testImplementation("org.testcontainers:junit-jupiter:1.17.3")

    testImplementation("org.wiremock.integrations.testcontainers:wiremock-testcontainers-module:1.0-alpha-14")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(libs.guava)

    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.0-rc1")

    implementation("org.slf4j:slf4j-api:1.7.30")

    implementation("ch.qos.logback:logback-classic:1.4.12")

    implementation("ch.qos.logback:logback-core:1.4.14")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.4")

    // https://mvnrepository.com/artifact/org.springframework/spring-webflux
    implementation("org.springframework:spring-webflux:6.1.13")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.0")

    implementation("io.github.resilience4j:resilience4j-spring-boot2:2.1.0")
    implementation("io.github.resilience4j:resilience4j-reactor:1.7.1")

    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation("org.springframework.boot:spring-boot-starter-cache:3.1.3")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
    implementation("org.springframework.boot:spring-boot-starter-validation:3.3.4")




}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}




tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

jacoco {
    toolVersion = "0.8.12"
    reportsDirectory = layout.buildDirectory.dir("jacocoReports")
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.withType<JacocoReport> {
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude("org/example/task3/**")
            exclude("org/example/task5/**")
            exclude("org/example/task8/dto/**")
        }
    )
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

