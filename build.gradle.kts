import net.fabricmc.loom.bootstrap.LoomGradlePluginBootstrap

object Constants {
    const val GROUP = "io.github.startsmercury"
    const val NAME = "simply-no-shading"
    const val VERSION: String = "7.5.0"

    const val SUBGROUP = "${GROUP}.${NAME}"

    const val DISPLAY_NAME: String = "Simply No Shading"

    const val VERSION_GAME = "1.21.2-alpha.24.35.a"
    const val VERSION_JAVA = "21"
    const val VERSION_MINECRAFT = "24w35a"
}

plugins {
    `java-library`
    id("fabric-loom") version "1.7.3"
}

val displayName by extra(Constants.DISPLAY_NAME)
val globalVersion = createVersionString()
val projectToBaseName: Map<String, String> by gradle.extra

base {
    group = Constants.GROUP
    archivesName = Constants.NAME
    version = globalVersion
}

subprojects {
    apply<JavaLibraryPlugin>()
    apply<LoomGradlePluginBootstrap>()

    val baseName = projectToBaseName[path]
    val displayName by extra("${Constants.DISPLAY_NAME}: $baseName")

    base {
        group = Constants.SUBGROUP
        archivesName = "${parent!!.base.archivesName.get()}-${name}"
        version = globalVersion
    }
}

allprojects {
    val displayName: String by extra

    loom {
        runtimeOnlyLog4j = true
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(Constants.VERSION_JAVA))
        }

        withJavadocJar()
        withSourcesJar()
    }

    dependencies {
        // Minecraft
        minecraft(group = "com.mojang", name = "minecraft", version = Constants.VERSION_MINECRAFT)

        // Obfuscation Mappings
        mappings(loom.officialMojangMappings())

        // Fabric Loader
        modImplementation(group = "net.fabricmc", name = "fabric-loader", version = "0.16.4")
    }

    tasks {
        val validateMixinName by registering(net.fabricmc.loom.task.ValidateMixinNameTask::class) {
            source(sourceSets.main.get().output)
        }

        withType<ProcessResources> {
            val data = mapOf(
                "displayName" to displayName,
                "gameVersion" to Constants.VERSION_GAME,
                "javaVersion" to Constants.VERSION_JAVA,
                "version" to version,
            )

            inputs.properties(data)

            filesMatching("fabric.mod.json") {
                expand(data)
            }
        }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<Javadoc> {
            options {
                this as StandardJavadocDocletOptions

                source = Constants.VERSION_JAVA
                encoding = "UTF-8"
                charSet = "UTF-8"
                memberLevel = JavadocMemberLevel.PACKAGE
                addStringOption("Xdoclint:none", "-quiet")
                tags(
                    "apiNote:a:API Note:",
                    "implSpec:a:Implementation Requirements:",
                    "implNote:a:Implementation Note:",
                )
            }

            include("com/github/startsmercury/simply/no/shading/**")
            include("**/api/**")
            isFailOnError = true
        }

        withType<Jar> {
            manifest {
                attributes(mapOf(
                    "Implementation-Title" to Constants.DISPLAY_NAME,
                    "Implementation-Version" to Constants.VERSION,
                    "Implementation-Vendor" to "StartsMercury",
                ))
            }
        }
    }
}

dependencies {
    subprojects {
        implementation(sourceSets.main.get().output)
    }
}

tasks {
    withType<Javadoc> {
        val main = allprojects.map { it.sourceSets.main.get() }

        source(main.map { it.allJava })
        classpath = files(main.map { it.compileClasspath })
    }

    withType<Jar> {
        val main = subprojects.map { it.sourceSets.main.get() }

        from(main.map { it.output }) {
            duplicatesStrategy = DuplicatesStrategy.WARN
        }
        dependsOn(main.map { it.classesTaskName })
    }
}

/******************************************************************************/
/* HELPER FUNCTIONS                                                           */
/******************************************************************************/

fun createVersionString(): String {
    val builder = StringBuilder()

    val isReleaseBuild = project.hasProperty("build.release")
    val buildId = System.getenv("GITHUB_RUN_NUMBER")

    if (isReleaseBuild) {
        builder.append(Constants.VERSION)
    } else {
        builder.append(Constants.VERSION.substringBefore('-'))
        builder.append("-snapshot")
    }

    builder.append("+mc").append(Constants.VERSION_MINECRAFT)

    if (!isReleaseBuild) {
        if (buildId != null) {
            builder.append("-build.${buildId}")
        } else {
            builder.append("-local")
        }
    }

    return builder.toString()
}
