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
    id("org.springframework.boot") version "3.3.4"
    checkstyle
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

checkstyle {
    toolVersion = "10.20.1"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
}


dependencies {
    implementation(project(":logtime"))

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.4")

    testImplementation("org.testcontainers:junit-jupiter:1.17.3")

    testImplementation("org.wiremock.integrations.testcontainers:wiremock-testcontainers-module:1.0-alpha-14")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

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

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.4")

    // https://mvnrepository.com/artifact/org.liquibase/liquibase-core
    implementation("org.liquibase:liquibase-core:4.29.2")

    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.7.4")

    // https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core
    implementation("org.hibernate.orm:hibernate-core:6.6.1.Final")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.0")
    
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security
    implementation("org.springframework.boot:spring-boot-starter-security:3.3.5")


    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // https://mvnrepository.com/artifact/org.springframework.security/spring-security-test
    testImplementation("org.springframework.security:spring-security-test:6.3.4")

    // https://mvnrepository.com/artifact/org.testcontainers/postgresql
    testImplementation("org.testcontainers:postgresql:1.20.3")
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

    reports {
        xml.required.set(true)
        csv.required.set(false)
    }
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
            exclude("org/example/task8/exception/**")
            exclude("org/example/task9/dto/**")
            exclude("org/example/task9/exception/**")
            exclude("org/example/task10/dto/**")
            exclude("org/example/task10/advice/**")
            exclude("org/example/task12/advice/**")
            exclude("org/example/task9/advice/**")
            exclude("org/example/task8/advice/**")
            exclude("org/example/task12/security/exceptions/**")
            exclude("org/example/task12/entity/**")
            exclude("org/example/task12/dto/**")
            exclude("org/example/task10/exception/**")
        }
    )
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

