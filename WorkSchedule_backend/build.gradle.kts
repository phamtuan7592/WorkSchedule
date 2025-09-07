plugins {
    alias(libs.plugins.kotlin.jvm)
    kotlin("plugin.serialization") version "1.9.21"
    alias(libs.plugins.ktor)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)

    // Content negotiation & serialization
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.11.1")

    // Testing
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    implementation("org.litote.kmongo:kmongo-coroutine:4.11.0")
    implementation("org.litote.kmongo:kmongo-serialization:4.11.0")
}