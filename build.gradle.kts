object Constants {
    const val MOD_NAME: String = "Simply No Shading"
    const val MOD_VERSION: String = "8.0.0-alpha.3"
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

    mods.register("simply-no-shading") {
        sourceSet("main")
        sourceSet("client")
    }
}

java {
    toolchain {
        languageVersion.convention(libs.versions.java.map(JavaLanguageVersion::of))
    }

    withJavadocJar()
    withSourcesJar()
}

dependencies {
    fun fabricModule(moduleName: String): Dependency? =
        implementation(fabricApi.module(moduleName, libs.versions.fabric.api.get()))

    minecraft(libs.minecraft)
    implementation(libs.fabric.loader)

    fabricModule("fabric-lifecycle-events-v1")
    fabricModule("fabric-key-mapping-api-v1")
    fabricModule("fabric-resource-loader-v0")
}

testing {
    @Suppress("UnstableApiUsage")
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

        withType<JvmTestSuite> { tasks.named("check") { dependsOn(targets.map { it.testTask }) } }
    }
}

tasks {
    val validateMixinName by registering(net.fabricmc.loom.task.ValidateMixinNameTask::class) {
        source(sourceSets.main.get().output)
        source(sourceSets.named("client").get().output)
    }

    withType<ProcessResources> {
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

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    javadoc {
        options {
            this as StandardJavadocDocletOptions

            source = libs.versions.java.get()
            encoding = "UTF-8"
            charSet = "UTF-8"
            memberLevel = JavadocMemberLevel.PRIVATE
            addStringOption("Xdoclint:none", "-quiet")
            tags(
                "apiNote:a:API Note:",
                "implSpec:a:Implementation Requirements:",
                "implNote:a:Implementation Note:",
            )
        }

        source(sourceSets.main.get().allJava)
        source(sourceSets.named("client").get().allJava)
        classpath = files(
            sourceSets.main.get().compileClasspath,
            sourceSets.named("client").get().compileClasspath
        )
        isFailOnError = true
    }

    jar {
        manifest {
            attributes(mapOf(
                "Implementation-Title" to Constants.MOD_NAME,
                "Implementation-Version" to Constants.MOD_VERSION,
                "Implementation-Vendor" to "StartsMercury",
            ))
        }
    }
}

/******************************************************************************/
/* COMPATIBILITY TESTS                                                        */
/******************************************************************************/

repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Terraformers Maven"
                url = uri("https://maven.terraformersmc.com")
            }
        }

        filter {
            includeGroup("com.terraformersmc")
        }
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "CaffeineMC Maven"
                url = uri("https://maven.caffeinemc.net/releases")
                mavenContent {
                    releasesOnly()
                }
            }
        }

        forRepository {
            maven {
                name = "CaffeineMC Snapshots Maven"
                url = uri("https://maven.caffeinemc.net/snapshots")
                mavenContent {
                    snapshotsOnly()
                }
            }
        }

        filter {
            includeGroup("net.caffeinemc")
        }
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth Maven"
                url = uri("https://api.modrinth.com/maven")
            }
        }

        filter {
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
}

// TODO Uncomment when those have builds for unobfuscated minecraft
// createCompatTest("bedrockify", libs.bedrockify)
// createCompatTest("enhancedblockentities", libs.enhancedblockentities)
// createCompatTest("iris", libs.iris, libs.sodium.fabric)
createCompatTest("modmenu", libs.modmenu)
// createCompatTest("sodium", libs.sodium.fabric)

dependencies {
    // TODO Uncomment when those have builds for unobfuscated minecraft
    // compileOnly(libs.sodium.api)
}

/******************************************************************************/
/* HELPER FUNCTIONS                                                           */
/******************************************************************************/

fun createCompatTest(name: String, objectNotation: Any, vararg dependencyNotations: Any) {
    val config = configurations.register(name)
    val configClasspath = configurations.register("${name}Classpath") {
        extendsFrom(config.get())
    }
    configurations {
        val compileOnly by getting {
            extendsFrom(config.get())
        }
    }

    dependencies {
        add(name, objectNotation)
        dependencyNotations.forEach {
            add("${name}Classpath", it)
        }
    }

    afterEvaluate {
        loom.runs.register(name) {
            client()

            property("fabric.addMods", configClasspath.get().files.joinToString(File.pathSeparator))
        }
    }
}

fun createVersionString(): String {
    val builder = StringBuilder()

    val isReleaseBuild = project.hasProperty("build.release")
    val buildId = System.getenv("GITHUB_RUN_NUMBER")

    if (isReleaseBuild) {
        builder.append(Constants.MOD_VERSION)
    } else {
        builder.append(Constants.MOD_VERSION.substringBefore('-'))
        builder.append("-snapshot")
    }

    builder.append("+mc").append(libs.versions.minecraft.get())

    if (!isReleaseBuild) {
        if (buildId != null) {
            builder.append("-build.${buildId}")
        } else {
            builder.append("-local")
        }
    }

    return builder.toString()
}
