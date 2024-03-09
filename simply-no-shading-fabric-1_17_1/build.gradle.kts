/******************************************************************************/
/* GRADLE PLUGINS                                                             */
/******************************************************************************/

plugins {
    id("fabric-loom")
}

/******************************************************************************/
/* PROJECT PROPERTIES                                                         */
/******************************************************************************/

val subgroup: String by rootProject.extra

val configureExtras: Action<Project> by rootProject.extra
configureExtras(project)

val configureJarTask: () -> Unit by extra
val configureJava: (Int) -> Action<JavaPluginExtension> by extra
val configureJavadocTask: () -> Unit by extra
val configureProcessResourcesTasks: () -> Unit by extra

group = subgroup
base.archivesName.set("simply-no-shading-fabric-1_17_1")

val gameVersion by extra("1.17.1")
val minecraftVersion by extra("1.17.1")

/******************************************************************************/
/* ADDITIONAL REPOSITORIES (see settings.gradle)                              */
/******************************************************************************/

repositories {
    maven {
        name = "Gegy Maven"
        url = uri("https://maven.gegy.dev")
        content {
            // spruce-ui
            includeGroup("dev.lambdaurora")
        }
    }

    maven {
        name = "shedaniel's Maven"
        url = uri("https://maven.shedaniel.me")
        content {
            // cloth-config-fabric
            includeGroup("me.shedaniel.cloth")
        }
    }

    maven {
        name = "Terraformers Maven"
        url = uri("https://maven.terraformersmc.com")
        content {
            // modmenu
            includeGroup("com.terraformersmc")
        }
    }

    // JitPack dependencies
    maven {
        name = "JitPack"
        url = uri("https://jitpack.io")
        content {

        }
    }

    // mod dependencies without dedicated repositories
    maven {
        name = "Modrinth Maven"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }

    // mod dependencies that aren't on modrinth or have corrupt version numbers
    ivy {
        name = "GitHub Releases"
        url = uri("https://github.com")
        patternLayout {
            artifact("[organization]/releases/download/[revision]/[module](-[classifier]).[ext]")
            artifact("[organization]/releases/download/[revision]/[module]-[revision](-[classsifier]).[ext]")
            setM2compatible(true)
        }
        metadataSources {
            artifact()
        }
    }
}

/******************************************************************************/
/* DEPENDENCIES                                                               */
/******************************************************************************/

java(configureJava(16))

loom {
    runtimeOnlyLog4j = true
}

dependencies {
    val fabricApiVersion = "0.46.1+1.17"
    fun net.fabricmc.loom.configuration.FabricApiExtension.module(moduleName: String): Dependency =
        fabricApi.module(moduleName, fabricApiVersion)

    // BedrockIfy
    modCompileOnly("juancarloscp52.BedrockIfy:bedrockify:1.2.0+mc1.17.1")

    // Deobfuscation Mappings (required)
    mappings(loom.officialMojangMappings())

    // Fabric API
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")

    // Fabric Lifecycle Events (v1)
    modCompileOnly(fabricApi.module("fabric-lifecycle-events-v1"))

    // Fabric Loader (required)
    modImplementation(libs.fabric.loader)

    // Fabric Key Binding API (v1)
    modCompileOnly(fabricApi.module("fabric-key-binding-api-v1"))

    // Fabric Resource Loader (v0)
    modCompileOnly(fabricApi.module("fabric-resource-loader-v0"))

    // Minecraft (required)
    minecraft("com.mojang:minecraft:${minecraftVersion}")

    // ModMenu
    modImplementation("com.terraformersmc:modmenu:2.0.17") {
        exclude(mapOf("module" to "fabric-loader"))
    }

    // Sodium
    modCompileOnly("maven.modrinth:sodium:mc1.17.1-0.3.4")

    // SpruceUI
    include(modImplementation("dev.lambdaurora:spruceui:3.3.2+1.17") {
        exclude(mapOf("module" to "fabric-loader"))
    })
}

/******************************************************************************/
/* TASK CONFIGURATIONS                                                        */
/******************************************************************************/

configureJarTask()
configureJavadocTask()
configureProcessResourcesTasks()
