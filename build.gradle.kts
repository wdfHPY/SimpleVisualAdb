import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Dependencies {
    private const val kotlinFlowCode = ""
    private const val kotlinCoroutineCode= "1.6.1"
    private const val loggerCode= "2.0.11"
    const val kotlinFlow = "$kotlinFlowCode"
    const val kotlinCoroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineCode"
    const val logger = "io.github.microutils:kotlin-logging-jvm:$loggerCode"
}

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "me.20132"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
//    testImplementation(kotlin("test"))
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:1.7.25")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")

//   testImplementation("ch.qos.logback:logback-classic:1.2.3")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12
    //    testImplementation("org.slf4j:slf4j-log4j12:1.7.25")

    implementation(compose.desktop.currentOs)
    implementation(Dependencies.kotlinCoroutine)
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation("org.slf4j:slf4j-api:1.7.25")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "SimpleVisualAdb"
            packageVersion = "1.0.0"
        }
    }
}