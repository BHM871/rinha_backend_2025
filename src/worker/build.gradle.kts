plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow.jar)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example"
version = "0.1"

application {
    mainClass = "com.example.worker.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.logback.classic)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.engine)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization)
    implementation(project(":src:models"))
    implementation(project(":src:redis"))
    testImplementation(libs.kotlin.test.junit)
}
