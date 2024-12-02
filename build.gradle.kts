plugins {
    id("fabric-loom") version "1.9.2"
    id("com.modrinth.minotaur") version "2.+"
}

val modName = property("mod.name").toString()
version = "${property("mod.version")}" + "+" + "${property("mod.version_name")}"
group = property("mod.maven_group").toString()


base {
    archivesName.set(modName)
}

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    mappings("net.fabricmc:yarn:${property("deps.yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${property("deps.cloth_config_version")}")
    modImplementation("com.terraformersmc:modmenu:${property("deps.mod_menu_version")}")

    implementation("org.lwjgl:lwjgl-glfw:3.3.2")
}

loom {
    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    runConfigs.all {
        ideConfigGenerated(true)
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
    }
}

val target = ">=${property("mod.min_target")}- <=${property("mod.max_target")}"

tasks.processResources {
    val expandProps = mapOf(
        "version" to project.version,
        "minecraftVersion" to target,
        "javaVersion" to project.property("deps.java")
    )

    filesMatching("fabric.mod.json") {
        expand(expandProps)
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
    loaders.addAll("fabric", "quilt")
    //featured = true

    dependencies {
        required.project("cloth-config")
        optional.project("modmenu")
    }

    val changes = rootProject.file("CHANGES.md").readText()
    changelog = if (project.property("deps.java") == "9") "# Requires Java 9+\n\n$changes" else changes
}