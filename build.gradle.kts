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
