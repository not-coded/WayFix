pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.5"
}

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true
    shared {
        fun mc(loader: String, vararg versions: String) {
            for (version in versions) vers("$version-$loader", version)
        }
        mc("fabric", "1.16.5", "1.19", "1.19.3", "1.20.6")
        mc("forge", "1.16.5", "1.19", "1.19.3", "1.20.1", "1.20.6")
        //WARNING: neoforge uses mods.toml instead of neoforge.mods.toml for versions 1.20.4 (?) and earlier
        mc("neoforge", "1.20.4", "1.20.6")
    }
    create(rootProject)
}

rootProject.name = "WayFix"