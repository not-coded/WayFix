pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")

        gradlePluginPortal()
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.5-beta.3"
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    shared {
        versions("1.16.5", "1.19", "1.19.3", "1.20.6")
        vcsVersion = "1.19.3"
    }

    create(rootProject)
}

rootProject.name = "WayFix"