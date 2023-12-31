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

val configureJava by extra {
    { version: Int ->
        Action<JavaPluginExtension> {
            toolchain {
                languageVersion.convention(JavaLanguageVersion.of(version))
            }

            withJavadocJar()
            withSourcesJar()
        }
    }
}
val createCompatTestFactory by extra {
    Action<Project> {
        val loom = extensions.findByName("loom")
        if (loom !is net.fabricmc.loom.api.LoomGradleExtensionAPI)
            return@Action

        /**
         * Creates similarly named source sets, remap configurations, and run
         * tasks for testing mod compatibility.
         *
         * @param name the base name
         * @param fosters the fosters to inherit from
        */
        val createCompatTest by extra {
            { name: String, fosters: Array<out String> ->
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
            }
        }
    }
}
val subgroup by extra { "${group}.${base.archivesName.get()}" }

group = "com.github.startsmercury"
base.archivesName.set("simply-no-shading")

/******************************************************************************/
/* DEPENDENCIES                                                               */
/******************************************************************************/

configureJava(8)
