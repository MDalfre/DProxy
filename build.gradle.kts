import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    kotlin("jvm") version "1.4.0"
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
    id ("no.tornado.fxlauncher") version "1.0.20"
}
group = "com.test"
version = "1.0-SNAPSHOT"

val tornadofx_version: String by rootProject

repositories {
    mavenCentral()
    jcenter()
    gradlePluginPortal()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven(
        url = "https://oss.sonatype.org/content/repositories/snapshots/"
    )
}

application {
    mainClassName = "com.dproxy.ui.MyApp"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("no.tornado:tornadofx:2.0.0-SNAPSHOT")
    testImplementation(kotlin("test-junit"))
    implementation("no.tornado:fxlauncher-gradle-plugin:1.0.20")

}
javafx { modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics") }

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "12"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "12"
    }
}

configure<no.tornado.fxlauncher.gradle.FXLauncherExtension> {
    applicationVendor = "Márcio Henrique Dalfré (marciodalfre@gmail.com)"
    applicationMainClass = application.mainClassName
    applicationVersion = "1.0.0"
    applicationUrl = "file://c/"
    applicationTitle = "DProxy Tunnel"
    applicationName = "DProxy"
    cacheDir = "deps"
    acceptDowngrade = false
}
