pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.minecraftforge.net")

        gradlePluginPortal()
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.5-beta.3"
}

stonecutter {
    kotlinController= true
    centralScript = "build.gradle.kts"

    shared {
        fun mc(mcVersion: String, loaders: Iterable<String>) {
            for (loader in loaders) {
                vers("$mcVersion-$loader", mcVersion)
            }
        }

        mc("1.16.5", listOf("fabric", "forge"))
        mc("1.19", listOf("fabric", "forge"))
        mc("1.19.3", listOf("fabric", "forge"))
        mc("1.20.6", listOf("fabric", "forge"/*, "neoforge"*/))

        vcsVersion = ("1.20.6-fabric")
    }
    create(rootProject)
}

rootProject.name = "WayFix"