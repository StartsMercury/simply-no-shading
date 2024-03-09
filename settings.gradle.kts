pluginManagement {
    repositories {
        // prefer to retrieve fabric loom plugin from here
        maven {
            name = "Fabric Maven"
            url = uri("https://maven.fabricmc.net")
        }

        // normal non-fabric plugins
        gradlePluginPortal()

        // for plugin dependency retrieval
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

include(
    "simply-no-shading-fabric-1_17_1",
    "simply-no-shading-fabric-1_18_2",
    "simply-no-shading-fabric-1_19_4",
    "simply-no-shading-fabric-1_20",
    "simply-no-shading-fabric-1_20_1",
    "simply-no-shading-fabric-1_20_2",
    "simply-no-shading-fabric-1_20_3",
    "simply-no-shading-fabric-1_20_4",
)
