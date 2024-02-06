/******************************************************************************/
/* GRADLE PLUGINS                                                             */
/******************************************************************************/

plugins {
    `java-library`
    id("fabric-loom") version "1.4.5" apply false
}

/******************************************************************************/
/* PROJECT PROPERTIES                                                         */
/******************************************************************************/

val configureExtras by extra {Action<Project> {
    val loom = extensions.findByName("loom") as net.fabricmc.loom.api.LoomGradleExtensionAPI?

    val configureJarTask by extra {{ ->
        tasks.named<Jar>("jar") {
            manifest {
                attributes(mapOf(
                    "Implementation-Title" to "Simply No Shading",
                    "Implementation-Version" to project.version,
                    "Implementation-Vendor" to "StartsMercury",
                ))
            }
        }
    }}

    val configureJava by extra {{ version: Int ->
        Action<JavaPluginExtension> {
            toolchain {
                languageVersion.convention(JavaLanguageVersion.of(version))
            }

            withJavadocJar()
            withSourcesJar()
        }
    }}

    val configureJavadocTask by extra {{ ->
        tasks.named<Javadoc>("javadoc") {
            if (loom?.areEnvironmentSourceSetsSplit() ?: false) {
                classpath += sourceSets["client"].compileClasspath
                source += sourceSets["client"].allJava
            }

            when (val options = this.options) {
                is CoreJavadocOptions -> options.addStringOption(
                    "tag",
                    "implNote:a:Implementation Note:",
                )
            }
        }
    }}

    val configureProcessResourcesTasks by extra {{ ->
        tasks.withType<ProcessResources> {
            val gameVersion: String by project.extra
            val loaderMinVersion = "0.15"
            val javaVersion = java.toolchain.languageVersion.get()
            val minecraftVersion: String by project.extra

            val data = mapOf(
                "gameVersion" to gameVersion,
                "javaVersion" to javaVersion,
                "loaderMinVersion" to loaderMinVersion,
                "minecraftVersion" to minecraftVersion,
                "version" to project.version,
            )

            inputs.properties(data)

            filesMatching("fabric.mod.json") {
                expand(data)
            }
        }
    }}

    if (loom != null) {
        /**
         * Creates similarly named source sets, remap configurations, and run
         * tasks for testing mod compatibility.
         *
         * @param name the base name
         * @param fosters the fosters to inherit from
        */
        val createCompatTest by extra {{ name: String, fosters: Array<out String> ->
            // TODO if-check if sources are split
            val sourcesSplit = loom.areEnvironmentSourceSetsSplit()

            val compatTest = sourceSets.create(
                "${name}CompatTest",
                loom::createRemapConfigurations
            )

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
        }}
    }
}}
val subgroup by extra { "${group}.${base.archivesName.get()}" }

configureExtras(project)

val configureJava: (Int) -> Action<JavaPluginExtension> by extra

group = "com.github.startsmercury"
base.archivesName = "simply-no-shading"

/******************************************************************************/
/* DEPENDENCIES                                                               */
/******************************************************************************/

configureJava(8)

/******************************************************************************/
/* TASK CONFIGURATIONS                                                        */
/******************************************************************************/
