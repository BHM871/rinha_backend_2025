plugins {
    alias(libs.plugins.kotlin.jvm)
    application
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
    testImplementation(libs.kotlin.test.junit)
}
