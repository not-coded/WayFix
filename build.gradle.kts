plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.modrinth.minotaur")
    id("com.github.johnrengelman.shadow")
}

val minecraft = stonecutter.current.version
val loader = loom.platform.get().name.lowercase()

version = "${mod.version}+${mod.prop("version_name")}-$loader"
group = mod.group
base {
    archivesName.set(mod.id)
}

val isFabric = loader == "fabric"
val isForge = loader == "forge"
val isNeoForge = loader == "neoforge"

stonecutter.const("fabric", isFabric)
stonecutter.const("forge", isForge)
stonecutter.const("neoforge", isNeoForge)

architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.prop("loom.platform")
})
repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.neoforged.net/releases")
    maven("https://maven.minecraftforge.net")

}
dependencies {
    minecraft("com.mojang:minecraft:$minecraft")

    if(isFabric || isForge) {
        mappings("net.fabricmc:yarn:$minecraft+build.${mod.dep("yarn_build")}:v2")
    }

    modImplementation("me.shedaniel.cloth:cloth-config-${loader}:${mod.dep("cloth_config_version")}")
    implementation("org.lwjgl:lwjgl-glfw:3.3.6")

    if (isFabric) {
        modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
        modImplementation("com.terraformersmc:modmenu:${mod.dep("mod_menu_version")}")
    }
    if (isForge) {
        "forge"("net.minecraftforge:forge:${minecraft}-${mod.dep("forge_loader")}")
    }
    if (isNeoForge) {
        "neoForge"("net.neoforged:neoforge:${mod.dep("neoforge_loader")}")
        mappings(loom.layered {
            mappings("net.fabricmc:yarn:$minecraft+build.${mod.dep("yarn_build")}:v2")
            mod.dep("neoforge_patch").takeUnless { it.startsWith('[') }?.let {
                mappings("dev.architectury:yarn-mappings-patch-neoforge:$it")
            }
        })
    }
}

loom {
    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }
    if (isForge) {
        forge.mixinConfig("wayfix.mixins.json")
    }
}

java {
    withSourcesJar()
    val javaVersion = mod.dep("java")
    val java = if (javaVersion == "8") JavaVersion.VERSION_1_8 else if(javaVersion == "17") JavaVersion.VERSION_17 else JavaVersion.VERSION_21
    targetCompatibility = java
    sourceCompatibility = java
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = "dev-shadow"
}

tasks.remapJar {
    injectAccessWidener = true
    input = tasks.shadowJar.get().archiveFile
    archiveClassifier = null
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    archiveClassifier = "dev"
}

val buildAndCollect = tasks.register<Copy>("buildAndCollect") {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(buildAndCollect)
    }

    rootProject.tasks.register("runActive") {
        group = "project"
        dependsOn(tasks.named("runClient"))
    }
}

tasks.processResources {

    properties(
        listOf("fabric.mod.json"),
        "version" to version,
        "minecraftVersion" to mod.prop("mc_dep_fabric"),
        "javaVersion" to mod.dep("java")
    )

    properties(
        listOf("META-INF/*mods.toml"),
        "version" to version,
        "minecraftVersion" to mod.prop("mc_dep_forgelike"),
        "javaVersion" to mod.dep("java")
    )
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("wayfix")

    versionNumber.set(version.toString())
    versionName.set("v${mod.version}+${mod.prop("version_name")} | ${loader.upperCaseFirst()}")
    versionType.set("release")
    uploadFile.set(tasks.remapJar)
    gameVersions.addAll(mod.prop("mc_targets").toString().split(","))
    //featured = true

    loaders.add(loader)
    if(isFabric) loaders.add("quilt")

    dependencies {
        required.project("cloth-config")
        if(isFabric) optional.project("modmenu")
    }

    changelog = rootProject.file("CHANGES.md").readText()
    if(minecraft == "1.16.5" && isForge) changelog = changelog.get() + "\nNOTE: You must disable the early loading screen manually, this can be done by adding \"-Dfml.earlyprogresswindow=false\" to your java arguments or by installing the [No Early loading progress](https://www.curseforge.com/minecraft/mc-mods/no-early-loading-progress) mod."
}