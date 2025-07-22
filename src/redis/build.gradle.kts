plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.logback.classic)
    implementation(libs.redis.client)
    implementation(libs.redis.pool)
    implementation(libs.ktor.serialization)
    implementation(project(":src:models"))
    testImplementation(libs.kotlin.test.junit)
}
