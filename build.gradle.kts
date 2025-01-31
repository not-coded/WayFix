import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.io.path.readText

plugins {
    id("fabric-loom") version "1.9.2"
    id("com.modrinth.minotaur") version "2.+"
}

version = property("mod_version")!!
group = property("maven_group")!!


base {
    archivesName.set(property("archives_base_name").toString())
}

repositories {
    flatDir { dirs("libraries") }

    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${property("cloth_config_version")}")
    modImplementation("com.terraformersmc:modmenu:${property("mod_menu_version")}")

    modImplementation("net.notcoded:codelib:${property("codelib_version")}")

    implementation("org.lwjgl:lwjgl-glfw:3.3.2")
}

tasks.processResources {
    val expandProps = mapOf(
        "version" to project.version,
    )

    filesMatching("fabric.mod.json") {
        expand(expandProps)
    }

    inputs.properties(expandProps)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(8)
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

project.gradle.addBuildListener(object : BuildListener {
    override fun settingsEvaluated(settings: Settings) { }

    override fun projectsLoaded(gradle: Gradle) { }

    override fun projectsEvaluated(gradle: Gradle) { }

    override fun buildFinished(result: BuildResult) {
        if (result.failure != null) return
        println("Fixing RefMaps")

        var jarFile = file(layout.buildDirectory.file("libs/wayfix-${project.version}.jar"))

        val env: MutableMap<String?, String?> = HashMap<String?, String?>()
        env.put("create", "true")

        val path: java.nio.file.Path = jarFile.toPath()
        val uri: URI? = URI.create("jar:" + path.toUri())

        FileSystems.newFileSystem(uri, env).use { fs ->
            val refMap: java.nio.file.Path? = fs.getPath("wayfix-refmap.json")
            if(refMap == null) return
            var refMapText = refMap.readText()

            // Fix refmap for the mixin WindowMixin116to1192:
            // Lnet/minecraft/class_1041;method_4491(Ljava/io/InputStream;Ljava/io/InputStream;)V

            refMapText = refMapText.replace(
                "    \"net/notcoded/wayfix/mixin/WindowMixin116to1192\": {\n" +
                        "      \"setIcon\": \"Lnet/minecraft/class_1041;method_4491(Lnet/minecraft/class_3262;Lnet/minecraft/class_8518;)V\"\n" +
                        "    },",
                "    \"net/notcoded/wayfix/mixin/WindowMixin116to1192\": {\n" +
                        "      \"setIcon\": \"Lnet/minecraft/class_1041;method_4491(Ljava/io/InputStream;Ljava/io/InputStream;)V\"\n" +
                        "    },"
            )

            refMapText = refMapText.replace(
                "      \"net/notcoded/wayfix/mixin/WindowMixin116to1192\": {\n" +
                        "        \"setIcon\": \"Lnet/minecraft/class_1041;method_4491(Lnet/minecraft/class_3262;Lnet/minecraft/class_8518;)V\"\n" +
                        "      },",
                "      \"net/notcoded/wayfix/mixin/WindowMixin116to1192\": {\n" +
                        "        \"setIcon\": \"Lnet/minecraft/class_1041;method_4491(Lnet/minecraft/class_1041;method_4491(Ljava/io/InputStream;Ljava/io/InputStream;)V\"\n" +
                        "      },"
            )

            // Fix refmap for the mixin WindowMixin1193to1194:
            // Lnet/minecraft/class_1041;method_4491(Lnet/minecraft/class_7367;Lnet/minecraft/class_7367;)V

            refMapText = refMapText.replace(
                "    \"net/notcoded/wayfix/mixin/WindowMixin1193to1194\": {\n" +
                        "      \"setIcon\": \"Lnet/minecraft/class_1041;method_4491(Lnet/minecraft/class_3262;Lnet/minecraft/class_8518;)V\"\n" +
                        "    },",
                "    \"net/notcoded/wayfix/mixin/WindowMixin1193to1194\": {\n" +
                        "      \"setIcon\": \"Lnet/minecraft/class_1041;method_4491(Lnet/minecraft/class_7367;Lnet/minecraft/class_7367;)V\"\n" +
                        "    },"
            )

            refMapText = refMapText.replace(
                "      \"net/notcoded/wayfix/mixin/WindowMixin1193to1194\": {\n" +
                        "        \"setIcon\": \"Lnet/minecraft/class_1041;method_4491(Lnet/minecraft/class_3262;Lnet/minecraft/class_8518;)V\"\n" +
                        "      },",
                "      \"net/notcoded/wayfix/mixin/WindowMixin1193to1194\": {\n" +
                        "        \"setIcon\": \"Lnet/minecraft/class_1041;method_4491(Lnet/minecraft/class_7367;Lnet/minecraft/class_7367;)V\"\n" +
                        "      },"
            )

            Files.newBufferedWriter(refMap, StandardCharsets.UTF_8, StandardOpenOption.WRITE).use { writer ->
                writer.write(refMapText)
            }
        }
    }
})


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

    changelog = rootProject.file("CHANGES.md").readText()
}