plugins {
    kotlin("jvm") version "2.1.10"
}

group = "com.example"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    testImplementation(libs.kotlin.test.junit)
}