
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.4.0"
    application

    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.0"
    id("org.jmailen.kotlinter") version "5.3.0"
    id("com.gradleup.shadow") version "9.4.3"
}

group = "be.zvz"
version = "0.0.2-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = java.sourceCompatibility

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(group = "io.ktor", name = "ktor-client-apache5", version = "3.5.1")
    implementation(group = "io.ktor", name = "ktor-client-content-negotiation", version = "3.5.1")
    implementation(group = "io.ktor", name = "ktor-client-encoding", version = "3.5.1")

    implementation(group = "io.ktor", name = "ktor-server-core-jvm", version = "3.5.1")
    implementation(group = "io.ktor", name = "ktor-server-netty-jvm", version = "3.5.1")
    implementation(group = "io.ktor", name = "ktor-server-compression", version = "3.5.1")
    implementation(group = "io.ktor", name = "ktor-server-compression-zstd", version = "3.5.1")

    implementation(group = "com.ensody.kompressor", name = "kompressor-ktor", version = "0.5.0")
    implementation(group = "com.ensody.kompressor", name = "kompressor-brotli-ktor", version = "0.5.0")

    implementation(group = "io.ktor", name = "ktor-server-content-negotiation", version = "3.5.1")
    implementation(group = "io.ktor", name = "ktor-server-forwarded-header", version = "3.5.1")
    implementation(group = "io.ktor", name = "ktor-serialization-kotlinx-json-jvm", version = "3.5.1")
    implementation(group = "io.ktor", name = "ktor-server-rate-limit", version = "3.5.1")
    implementation(group = "io.ktor", name = "ktor-server-cors", version = "3.5.1")

    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.5.37")
    implementation(group = "org.fusesource.jansi", name = "jansi", version = "2.4.3")

    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8")
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-reflect")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(java.sourceCompatibility.toString()))
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClass.set("be.zvz.ytmbrowseproxy.ApplicationKt")
}
