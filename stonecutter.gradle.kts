plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.20.6-fabric" /* [SC] DO NOT EDIT */

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

stonecutter registerChiseled tasks.register("chiseledModrinth", stonecutter.chiseled) {
    group = "project"
    ofTask("modrinth")
}
