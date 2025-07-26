plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow.jar)
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
    implementation(project(":src:models"))
    implementation(project(":src:redis"))
    testImplementation(libs.kotlin.test.junit)
}
