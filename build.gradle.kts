import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Dependencies {
    private const val kotlinFlowCode = ""
    private const val kotlinCoroutineCode= "1.6.1"
    const val kotlinFlow = "$kotlinFlowCode"
    const val kotlinCoroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineCode"
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
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
    implementation(Dependencies.kotlinCoroutine)
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