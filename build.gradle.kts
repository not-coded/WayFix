plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.modrinth.minotaur") version "2.+"
}

val minecraft = stonecutter.current.version
version = "${mod.version}+${mod.version_name}-common"
group = mod.maven_group

base {
    archivesName.set(mod.name)
}

architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.prop("loom.platform")
})

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft")
    mappings("net.fabricmc:yarn:${mod.dep("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${mod.dep("cloth_config_version")}")
    modImplementation("com.terraformersmc:modmenu:${mod.dep("mod_menu_version")}")

    implementation("org.lwjgl:lwjgl-glfw:3.3.2")
}

loom {
    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }
}

java {
    withSourcesJar()

    val javaVersion = if (mod.dep("java") == "9") JavaVersion.VERSION_1_9 else JavaVersion.VERSION_17

    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}


modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("wayfix")
    versionNumber.set(version.toString())
    versionName.set("v$version")
    versionType.set("release")
    uploadFile.set(tasks.remapJar)
    gameVersions.addAll(property("publishing.supported_versions").toString().split(","))
    loaders.addAll("fabric", "quilt")
    //featured = true

    dependencies {
        required.project("cloth-config")
        optional.project("modmenu")
    }

    val changes = rootProject.file("CHANGES.md").readText()
    changelog = if (mod.dep("java") == "9") "# Requires Java 9+\n\n$changes" else changes
}