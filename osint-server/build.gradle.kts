plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.ktor.plugin") version "2.3.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    application
}

group = "io.osint"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.6")
    implementation("io.ktor:ktor-server-netty:2.3.6")
    implementation("io.ktor:ktor-server-cors:2.3.6")
    implementation("io.ktor:ktor-client-cio:2.3.0")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.6")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.6")
    implementation("io.ktor:ktor-serialization-jackson:2.3.3")
    implementation("io.ktor:ktor-server-call-logging:2.3.3")

    implementation("org.jetbrains.exposed:exposed-core:0.40.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
    implementation("org.postgresql:postgresql:42.5.1")

    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.0")


    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.ktor:ktor-server-tests:2.3.6")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

application {
    mainClass.set("io.osint.MainKt")
}


tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Jar> {
    archiveFileName.set("server.jar")
}
