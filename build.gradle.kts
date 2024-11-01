plugins {
    id("dev.architectury.loom") version "1.7.+"
    id("com.modrinth.minotaur") version "2.+"
}

val modName = property("mod.name").toString()

val loader = loom.platform.get().name.lowercase()
val isFabric = loader == "fabric"
val isForge = loader == "forge"

version = "${property("mod.version")}" + "+" + "${property("mod.version_name")}" + "-" + loader
group = property("mod.maven_group").toString()

stonecutter.const("fabric", isFabric)
stonecutter.const("forge", isForge)

base {
    archivesName.set(modName)
}

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.neoforged.net/releases")
    maven("https://maven.minecraftforge.net")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    if(isFabric || isForge) {
        mappings("net.fabricmc:yarn:${property("deps.yarn_mappings")}:v2")
    }

    modImplementation("me.shedaniel.cloth:cloth-config-${loader}:${property("deps.cloth_config_version")}")
    implementation("org.lwjgl:lwjgl-glfw:3.3.2")

    if(isFabric) {
        modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
        modImplementation("com.terraformersmc:modmenu:${property("deps.mod_menu_version")}")
    } else if (isForge) {
        "forge"("net.minecraftforge:forge:${property("deps.minecraft")}-${property("deps.forge_loader")}")
    }

}

loom {
    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    /*
    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
    }

    runConfigs.remove(runConfigs["server"])

     */
}

val target = ">=${property("mod.min_target")}- <=${property("mod.max_target")}"

tasks.processResources {
    val expandProps = mapOf(
        "version" to project.version,
        "minecraftVersion" to target,
        "javaVersion" to project.property("deps.java")
    )

    if (isFabric) {
        filesMatching("fabric.mod.json") { expand(expandProps) }
        exclude("META-INF/mods.toml", "META-INF/neoforge.mods.toml", "pack.mcmeta")
    }

    if(isForge) {
        filesMatching("META-INF/*mods.toml") { expand(expandProps) }
        exclude("fabric.mod.json")
    }

    inputs.properties(expandProps)
}

java {
    withSourcesJar()

    val javaVersion = if (project.property("deps.java") == "9") JavaVersion.VERSION_1_9 else JavaVersion.VERSION_17

    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs"))
    dependsOn("build")
}


modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("wayfix")
    versionNumber.set(version.toString())
    versionName.set("v$version")
    versionType.set("release")
    uploadFile.set(tasks.remapJar)
    gameVersions.addAll(property("publishing.supported_versions").toString().split(","))
    if(isFabric) loaders.addAll("fabric", "quilt")
    else if(isForge) {
        loaders.add("forge")
        if(property("mod.version").toString() == "1.20") loaders.add("neoforge")
    }
    //featured = true

    dependencies {
        required.project("cloth-config")
        if(isFabric) optional.project("modmenu")
    }

    val changes = rootProject.file("CHANGES.md").readText()
    changelog = if (project.property("deps.java") == "9") "# Requires Java 9+\n\n$changes" else changes
}