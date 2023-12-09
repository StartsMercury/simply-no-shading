/******************************************************************************/
/* GRADLE PLUGINS                                                             */
/******************************************************************************/

plugins {
    // only used to make this project available on JitPack
    // https://docs.jitpack.io/building/#gradle-projects
    `maven-publish`

    // configures a specialized development environment for fabric mods
    alias(libs.plugins.fabric.loom)
}

/******************************************************************************/
/* PROJECT PROPERTIES                                                         */
/******************************************************************************/

fun requiredStringProperty(key: String): String = project.property(key)!!.toString()

val baseVersion = requiredStringProperty("baseVersion")

project.group = "com.github.startsmercury"
base.archivesName.set("simply-no-shading")
if (project.version == DEFAULT_VERSION) project.version = "$baseVersion-mc${libs.versions.minecraft.get()}"

/******************************************************************************/
/* ADDITIONAL REPOSITORIES (see settings.gradle)                              */
/******************************************************************************/

repositories {
    maven {
        name = "Gegy"
        url = uri("https://maven.gegy.dev")
        content {
            includeGroup("dev.lambdaurora")
        }
    }

    maven {
        name = "shedaniel's"
        url = uri("https://maven.shedaniel.me")
        content {
            includeGroup("me.shedaniel.cloth")
        }
    }

    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com")
        content {
            includeGroup("com.terraformersmc")
        }
    }

    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }

    ivy {
        name = "GitHub Releases"
        url = uri("https://github.com")
        patternLayout {
            artifact("[organization]/releases/download/[revision]/[module](-[classifier]).[ext]")
            artifact("[organization]/releases/download/[revision]/[module]-[revision](-[classifier]).[ext]")
            setM2compatible(true)
        }
        metadataSources {
            artifact()
        }
    }

    maven {
        name = "Devan"
        url = uri("https://raw.githubusercontent.com/Devan-Kerman/Devan-Repo/master")
        content {
            includeGroup("net.devtech")
        }
    }
}

/******************************************************************************/
/* DEPENDENCIES                                                               */
/******************************************************************************/

java {
    toolchain {
        languageVersion.convention(libs.versions.java.map(JavaLanguageVersion::of))
    }

    withJavadocJar()
    withSourcesJar()
}

loom {
    runtimeOnlyLog4j = true
}

configurations.named("modRuntimeOnly") {
    exclude(mapOf("module" to "fabric-loader"))
}

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

    loom.runs.register("${name}CompatTestClient") {
        client()
        source("${name}CompatTest")
    }

    loom.runs.register("${name}CompatTestServer") {
        server()
        source("${name}CompatTest")
    }

    fun String.capitalize(): String = this.replaceFirstChar(Character::toTitleCase)

    configurations.named("mod${name.capitalize()}CompatTestRuntimeOnly") {
        exclude(mapOf("module" to "fabric-loader"))
    }

    val modAuto = configurations.create("mod${name.capitalize()}Auto")

    val modCompileOnly by configurations.getting {
        extendsFrom(modAuto)
    }

    configurations.named("mod${name.capitalize()}CompatTestImplementation") {
        extendsFrom(modAuto)
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
    "modEnhancedblockentitiesCompatTestRuntimeOnly"(libs.arrp)

    // BedrockIfy
    "modBedrockifyAuto"(libs.bedrockify)

    // Cloth Config
    "modBedrockifyCompatTestRuntimeOnly"(libs.cloth.config) {
        exclude(mapOf("group" to "net.fabricmc.fabric-api"))
        exclude(mapOf("module" to "gson"))
    }

    // Deobfuscation Mappings (required)
    mappings(loom.officialMojangMappings())

    // Enhanced Block Entities
    "modEnhancedblockentitiesAuto"(libs.enhancedblockentities)

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
    "modIndiumAuto"(libs.indium)

    // JOML
    "modSodiumCompatTestRuntimeOnly"(libs.joml)

    // Minecraft (required)
    minecraft(libs.minecraft)

    // ModMenu
    "modImplementation"(libs.modmenu)

    // Sodium
    "modSodiumAuto"(libs.sodium)

    // SpruceUI
    include(libs.spruceui)
    "modImplementation"(libs.spruceui)
}

/******************************************************************************/
/* TASK CONFIGURATIONS                                                        */
/******************************************************************************/

tasks.named<Jar>("jar") {
    manifest {
        attributes(mapOf(
            "Implementation-Title" to "Simply No Shading",
            "Implementation-Version" to baseVersion,
            "Implementation-Vendor" to "StartsMercury",
        ))
    }
}

tasks.named<Javadoc>("javadoc") {
    when (val options = this.options) {
        is CoreJavadocOptions -> options.addStringOption("tag", "implNote:a:Implementation Note:")
    }
}

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

/******************************************************************************/
/* MAVEN PUBLISHING                                                           */
/******************************************************************************/

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for
    // information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in
        //         the top level. The repositories here will be used for
        //         publishing your artifact, not for retrieving dependencies.
    }
}
