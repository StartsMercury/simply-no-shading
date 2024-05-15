object Constants {
    const val MOD_VERSION: String = "7.0.0"
}

plugins {
    alias(libs.plugins.fabric.loom)
}

base {
    group = "io.github.startsmercury"
    archivesName = "simply-no-shading"
    version = createVersionString()
}

loom {
    runtimeOnlyLog4j = true

    splitEnvironmentSourceSets()
}

java {
    toolchain {
        languageVersion.convention(libs.versions.java.map(JavaLanguageVersion::of))
    }

    withJavadocJar()
    withSourcesJar()
}

/******************************************************************************/
/* DEPENDENCIES                                                               */
/******************************************************************************/

repositories {
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

createCompatTest("bedrockify")
createCompatTest("enhancedblockentities")
createCompatTest("sodium")
createCompatTest("indium", "sodium")

dependencies {
    fun net.fabricmc.loom.configuration.FabricApiExtension.module(moduleName: String): Dependency =
        fabricApi.module(moduleName, libs.versions.fabric.api.get())

    // ARRP
    "modEnhancedblockentitiesCompatTestClientRuntimeOnly"(libs.arrp) {
        exclude(mapOf("module" to "fabric-loader"))
    }

    // BedrockIfy
    "modBedrockifyAuto"(libs.bedrockify)

    // Cloth Config
    "modBedrockifyCompatTestClientRuntimeOnly"(libs.cloth.config) {
        exclude(mapOf("group" to "net.fabricmc.fabric-api"))
        exclude(mapOf("module" to "fabric-loader"))
        exclude(mapOf("module" to "gson"))
    }

    // Deobfuscation Mappings (required)
    mappings(loom.officialMojangMappings())

    // Enhanced Block Entities
    "modEnhancedblockentitiesClientAuto"(libs.enhancedblockentities)

    // Fabric API
    modRuntimeOnly(libs.fabric.api)

    // Fabric Lifecycle Events (v1)
    modCompileOnly(fabricApi.module("fabric-lifecycle-events-v1"))

    // Fabric Loader (required)
    modImplementation(libs.fabric.loader)

    // Fabric Key Binding API (v1)
    modCompileOnly(fabricApi.module("fabric-key-binding-api-v1"))

    // Fabric Resource Loader (v0)
    modCompileOnly(fabricApi.module("fabric-resource-loader-v0"))

    // Indium
    "modIndiumClientAuto"(libs.indium)

    // Minecraft (required)
    minecraft(libs.minecraft)

    // ModMenu
    "modClientImplementation"(libs.modmenu) {
        exclude(mapOf("module" to "fabric-loader"))
    }

    // Sodium
    "modSodiumClientAuto"(libs.sodium)
}

/******************************************************************************/
/* TESTING SUITES                                                             */
/******************************************************************************/

testing {
    suites {
        val clientTest by registering(JvmTestSuite::class) {
            val client by sourceSets.getting

            sources {
                compileClasspath += client.compileClasspath
                runtimeClasspath += client.runtimeClasspath
            }

            dependencies {
                implementation(client.output)
            }
        }
    }
}

/******************************************************************************/
/* TASK CONFIGURATIONS                                                        */
/******************************************************************************/

tasks.withType<ProcessResources> {
    val data = mapOf(
        "gameVersion" to libs.versions.fabric.minecraft.get(),
        "javaVersion" to libs.versions.java.get(),
        "minecraftVersion" to libs.versions.minecraft.get(),
        "version" to version,
    )

    inputs.properties(data)

    filesMatching("fabric.mod.json") {
        expand(data)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named<Javadoc>("javadoc") {
    classpath += sourceSets["client"].compileClasspath
    source += sourceSets["client"].allJava

    when (val options = this.options) {
        is StandardJavadocDocletOptions -> options.tags(
            "apiNote:a:API Note",
            "implNote:a:Implementation Note",
            "implSpec:a:Implementation Requirements",
        )
    }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(mapOf(
            "Implementation-Title" to "Simply No Shading",
            "Implementation-Version" to Constants.MOD_VERSION,
            "Implementation-Vendor" to "StartsMercury",
        ))
    }
}

/******************************************************************************/
/* HELPER FUNCTIONS                                                           */
/******************************************************************************/

/**
 * Creates similarly named source sets, remap configurations, and run tasks for
 * testing mod compatibility.
 *
 * @param name the base name
 * @param fosters the fosters to inherit from
 */
fun createCompatTest(name: String, vararg fosters: String) {
    val compatTest = sourceSets.create("${name}CompatTest", loom::createRemapConfigurations)

    sourceSets.main {
        compatTest.compileClasspath += this.compileClasspath
        compatTest.runtimeClasspath += this.runtimeClasspath
    }

    for (foster in fosters) {
        sourceSets.named("${foster}CompatTest") {
            compatTest.compileClasspath += this.compileClasspath
            compatTest.runtimeClasspath += this.runtimeClasspath
        }
    }

    val compatTestClient = sourceSets.create("${name}CompatTestClient") {
        net.fabricmc.loom.configuration.RemapConfigurations.configureClientConfigurations(
            project,
            this
        );
    }

    sourceSets.main {
        compatTestClient.compileClasspath += this.compileClasspath
        compatTestClient.runtimeClasspath += this.runtimeClasspath
    }

    val client by sourceSets.getting {
        compatTestClient.compileClasspath += this.compileClasspath
        compatTestClient.runtimeClasspath += this.runtimeClasspath
    }

    compatTestClient.compileClasspath += compatTest.compileClasspath
    compatTestClient.runtimeClasspath += compatTest.runtimeClasspath

    for (foster in fosters) {
        sourceSets.named("${foster}CompatTestClient") {
            compatTestClient.compileClasspath += this.compileClasspath
            compatTestClient.runtimeClasspath += this.runtimeClasspath
        }
    }

    loom.runs.register("${name}CompatTestClient") {
        client()
        source("${name}CompatTestClient")
    }

    loom.runs.register("${name}CompatTestServer") {
        server()
        source("${name}CompatTest")
    }

    fun String.capitalize(): String = this.replaceFirstChar(Character::toTitleCase)

    val modAuto = configurations.create("mod${name.capitalize()}Auto")

    val modCompileOnly by configurations.getting {
        extendsFrom(modAuto)
    }

    configurations.named("mod${name.capitalize()}CompatTestImplementation") {
        extendsFrom(modAuto)
    }

    val modClientAuto = configurations.create("mod${name.capitalize()}ClientAuto")

    val modClientCompileOnly by configurations.getting {
        extendsFrom(modClientAuto)
    }

    configurations.named("mod${name.capitalize()}CompatTestClientImplementation") {
        extendsFrom(modAuto)
    }
}

/**
 * Helper function to construct the complete version.
 */
fun createVersionString(): String {
    val version = version

    return if (version is String && version != DEFAULT_VERSION) {
        version
    } else {
        val mod = Constants.MOD_VERSION
        val minecraft = libs.versions.fabric.minecraft

        "$mod+mc$minecraft"
    }
}
