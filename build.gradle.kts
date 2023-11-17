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
project.version = "$baseVersion+${libs.versions.minecraft.get()}"

/******************************************************************************/
/* ADDITIONAL REPOSITORIES (see settings.gradle)                              */
/******************************************************************************/

repositories {
    maven {
        name = "AperLambda Maven"
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
            artifact("[organization]/releases/download/[revision]/[module]-[revision](-[classifier]).[ext]")
            setM2compatible(true)
        }
        metadataSources {
            artifact()
        }
    }

    maven {
        name = "Devan Maven"
        url = uri("https://raw.githubusercontent.com/Devan-Kerman/Devan-Repo/master")
        content {
            // arrp 0.4.2 or older
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
    // runtimeOnlyLog4j = true

    // splitEnvironmentSourceSets()
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

    val modAuto = configurations.create("mod${name.capitalize()}Auto")

    val modCompileOnly by configurations.getting {
        extendsFrom(modAuto)
    }

    configurations.named("mod${name.capitalize()}CompatTestImplementation") {
        extendsFrom(modAuto)
    }
}

createCompatTest("sodium")

dependencies {
    fun net.fabricmc.loom.configuration.FabricApiExtension.module(moduleName: String): Dependency =
        fabricApi.module(moduleName, libs.versions.fabric.api.get())

    // AperLambda λCommon
    include(libs.lambdacommon)
    runtimeOnly(libs.lambdacommon) {
        exclude(mapOf("group" to "com.google.code.gson"))
        exclude(mapOf("group" to "com.google.guava"))
    }

    // Deobfuscation Mappings (required)
    mappings(loom.officialMojangMappings())

    // Fabric API
    modRuntimeOnly(libs.fabric.api) {
        exclude(mapOf("module" to "fabric-loader"))
    }

    // Fabric API Base
    modCompileOnly(fabricApi.module("fabric-api-base"))

    // Fabric Lifecycle Events (v1)
    modCompileOnly(fabricApi.module("fabric-lifecycle-events-v1"))

    // Fabric Loader (required)
    modImplementation(libs.fabric.loader)

    // Fabric Key Binding API (v1)
    modCompileOnly(fabricApi.module("fabric-key-binding-api-v1"))

    // Fabric Resource Loader (v0)
    modCompileOnly(fabricApi.module("fabric-resource-loader-v0"))

    // Minecraft (required)
    minecraft(libs.minecraft)

    // ModMenu
    "modImplementation"(libs.modmenu) {
        exclude(mapOf("module" to "fabric-loader"))
    }

    // Sodium
    "modSodiumAuto"(libs.sodium)

    // SpruceUI
    include(libs.spruceui)
    "modImplementation"(libs.spruceui) {
        exclude(mapOf("module" to "fabric-loader"))
    }
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
