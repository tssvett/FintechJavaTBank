plugins {
    `java-library`
}

group = "org.example.task5"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:5.3.10")

    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("org.slf4j:slf4j-api:1.7.30")

    implementation("ch.qos.logback:logback-classic:1.4.12")

    implementation("ch.qos.logback:logback-core:1.4.14")

    implementation("org.springframework.boot:spring-boot-starter-web:3.3.4")
}

// Настройки компиляции Java
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}