pluginManagement {
    repositories {
        // prefer to retrieve fabric loom plugin from here
        maven {
            name = "Fabric"
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

