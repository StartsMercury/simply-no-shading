pluginManagement {
    repositories {
        maven {
            name = "Fabric Maven"
            url = uri("https://maven.fabricmc.net")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

val projectToBaseName by gradle.extra(
    mapOf(
        "base" to "Base",
    ).mapKeys { (name, _) ->
        include(name)

        val path = ":${name}"

        project(path).projectDir = file("subprojects/${name}")

        return@mapKeys path
    }
)
