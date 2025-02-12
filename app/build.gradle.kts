
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.21"
    application

    id("org.jmailen.kotlinter") version "4.5.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "be.zvz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = java.sourceCompatibility

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(group = "io.ktor", name = "ktor-client-apache", version = "3.1.0")
    implementation(group = "io.ktor", name = "ktor-client-content-negotiation", version = "3.0.3")

    implementation(group = "io.ktor", name = "ktor-server-core-jvm", version = "3.0.3")
    implementation(group = "io.ktor", name = "ktor-server-netty-jvm", version = "3.0.3")
    implementation(group = "io.ktor", name = "ktor-server-content-negotiation", version = "3.0.3")
    implementation(group = "io.ktor", name = "ktor-server-forwarded-header", version = "3.0.3")
    implementation(group = "io.ktor", name = "ktor-serialization-jackson", version = "3.0.3")
    implementation(group = "io.ktor", name = "ktor-server-rate-limit", version = "3.0.3")
    implementation(group = "io.ktor", name = "ktor-server-cors", version = "3.0.3")

    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.18.2")
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-blackbird", version = "2.18.2")

    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.5.15")

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
