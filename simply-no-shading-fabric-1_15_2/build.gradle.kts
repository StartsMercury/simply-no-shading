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
base.archivesName.set("simply-no-shading-fabric-1_15_2")

val gameVersion by extra("1.15.2")
val minecraftVersion by extra("1.15.2")

/******************************************************************************/
/* ADDITIONAL REPOSITORIES (see settings.gradle)                              */
/******************************************************************************/

repositories {
    maven {
        name = "AperLambda"
        url = uri("https://aperlambda.github.io/maven")
        content {
            // lambdajcommon
            includeGroup("org.aperlambda")
        }
    }

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

java(configureJava(8))

dependencies {
    val fabricApiVersion = "0.28.5+1.15"
    fun net.fabricmc.loom.configuration.FabricApiExtension.module(moduleName: String): Dependency =
        fabricApi.module(moduleName, fabricApiVersion)

    // AperLambda λCommon
    include(runtimeOnly("org.aperlambda:lambdajcommon:1.8.0") {
        exclude(mapOf("group" to "com.google.code.gson"))
        exclude(mapOf("group" to "com.google.guava"))
    })

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
    modImplementation("com.terraformersmc:modmenu:1.10.7") {
        exclude(mapOf("module" to "fabric-loader"))
    }

    // Sodium
    modCompileOnly("mrmangohands.sodium-fabric:sodium-1.15.2-backport-fabric-0.1.1-SNAPSHOT+2020-12-10")

    // SpruceUI
    include(modImplementation("LambdAurora.SpruceUI:spruceui:1.3.4") {
        exclude(mapOf("module" to "fabric-loader"))
    })
}

/******************************************************************************/
/* TASK CONFIGURATIONS                                                        */
/******************************************************************************/

configureJarTask()
configureJavadocTask()
configureProcessResourcesTasks()
