/******************************************************************************/
/* GRADLE PLUGINS                                                             */
/******************************************************************************/

plugins {
    // configures a specialized development environment for fabric mods
    id("fabric-loom")
}

/******************************************************************************/
/* PROJECT PROPERTIES                                                         */
/******************************************************************************/

val configureJava: (Int) -> Action<JavaPluginExtension> by rootProject.extra
val createCompatTestFactory: Action<Project> by rootProject.extra
val subgroup: String by rootProject.extra

group = subgroup
base.archivesName.set("simply-no-shading-fabric-1_20_4")

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

java(configureJava(17))

loom {
    runtimeOnlyLog4j = true

    splitEnvironmentSourceSets()
}

createCompatTestFactory(project)
val createCompatTest: (String, Array<out String>) -> Unit by extra

createCompatTest("bedrockify", arrayOf())
createCompatTest("enhancedblockentities", arrayOf())
createCompatTest("sodium", arrayOf())
createCompatTest("indium", arrayOf("sodium"))

dependencies {
    val version_fabric_api = "0.91.3+1.20.4"
    fun net.fabricmc.loom.configuration.FabricApiExtension.module(moduleName: String): Dependency =
        fabricApi.module(moduleName, version_fabric_api)

    // ARRP
    "modEnhancedblockentitiesCompatTestClientRuntimeOnly"("maven.modrinth:arrp:0.8.0") {
        exclude(mapOf("module" to "fabric-loader"))
    }

    // BedrockIfy
    "modBedrockifyAuto"("maven.modrinth:bedrockify:1.9.1+mc1.20.2")

    // Cloth Config
    "modBedrockifyCompatTestClientRuntimeOnly"("me.shedaniel.cloth:cloth-config-fabric:12.0.109") {
        exclude(mapOf("group" to "net.fabricmc.fabric-api"))
        exclude(mapOf("module" to "fabric-loader"))
        exclude(mapOf("module" to "gson"))
    }

    // Deobfuscation Mappings (required)
    mappings(loom.officialMojangMappings())

    // Enhanced Block Entities
    "modEnhancedblockentitiesClientAuto"("maven.modrinth:ebe:0.9.1+1.20.2")

    // Fabric API
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${version_fabric_api}")

    // Fabric Lifecycle Events (v1)
    modCompileOnly(fabricApi.module("fabric-lifecycle-events-v1"))

    // Fabric Loader (required)
    modImplementation(libs.fabric.loader)

    // Fabric Key Binding API (v1)
    modCompileOnly(fabricApi.module("fabric-key-binding-api-v1"))

    // Fabric Resource Loader (v0)
    modCompileOnly(fabricApi.module("fabric-resource-loader-v0"))

    // Indium
    "modIndiumClientAuto"("maven.modrinth:indium:1.0.28+mc1.20.4")

    // Minecraft (required)
    minecraft("com.mojang:minecraft:1.20.4")

    // ModMenu
    "modClientImplementation"("com.terraformersmc:modmenu:9.0.0") {
        exclude(mapOf("module" to "fabric-loader"))
    }

    // Sodium
    "modSodiumClientAuto"("maven.modrinth:sodium:mc1.20.3-0.5.5")

    // SpruceUI
    include("modClientImplementation"("dev.lambdaurora:spruceui:5.0.3+1.20.4") {
        exclude(mapOf("module" to "fabric-loader"))
    })
}

/******************************************************************************/
/* TASK CONFIGURATIONS                                                        */
/******************************************************************************/

tasks.named<Jar>("jar") {
    manifest {
        attributes(mapOf(
            "Implementation-Title" to "Simply No Shading",
            "Implementation-Version" to version,
            "Implementation-Vendor" to "StartsMercury",
        ))
    }
}

tasks.named<Javadoc>("javadoc") {
    classpath += sourceSets["client"].compileClasspath
    source += sourceSets["client"].allJava

    when (val options = this.options) {
        is CoreJavadocOptions -> options.addStringOption("tag", "implNote:a:Implementation Note:")
    }
}

tasks.withType<ProcessResources> {
    val data = mapOf(
        "gameVersion" to "1.20.4",
        "javaVersion" to "17",
        "minecraftVersion" to "1.20.4",
        "version" to version,
    )

    inputs.properties(data)

    filesMatching("fabric.mod.json") {
        expand(data)
    }
}
