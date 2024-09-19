object Constants {
    const val MOD_NAME: String = "Simply No Shading"
    const val MOD_VERSION: String = "7.6.0"
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

dependencies {
    fun fabricModule(moduleName: String): Dependency? =
        modCompileOnly(fabricApi.module(moduleName, libs.versions.fabric.api.get()))

    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabric.loader)

    modImplementation(libs.fabric.api)
    fabricModule("fabric-lifecycle-events-v1")
    fabricModule("fabric-key-binding-api-v1")
    fabricModule("fabric-resource-loader-v0")
    @Suppress("UnstableApiUsage")
    "modClientImplementation"(libs.modmenu) {
        exclude(mapOf("module" to "fabric-loader"))
    }
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
            memberLevel = JavadocMemberLevel.PACKAGE
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
        include("com/github/startsmercury/simply/no/shading/**")
        include("**/api/**")
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

createCompatTest("bedrockify")
createCompatTest("enhancedblockentities")
createCompatTest("sodium")

repositories {
    maven {
        name = "shedaniel's Maven"
        url = uri("https://maven.shedaniel.me")
        content {
            includeGroup("me.shedaniel.cloth")
        }
    }

    maven {
        name = "Terraformers Maven"
        url = uri("https://maven.terraformersmc.com")
        content {
            includeGroup("com.terraformersmc")
        }
    }

    maven {
        name = "Modrinth Maven"
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
}

@Suppress("UnstableApiUsage")
dependencies {
    "modBedrockifyAuto"(libs.bedrockify)
    "modBedrockifyCompatTestClientRuntimeOnly"(libs.cloth.config) {
        exclude(mapOf("group" to "net.fabricmc.fabric-api"))
        exclude(mapOf("module" to "fabric-loader"))
        exclude(mapOf("module" to "gson"))
    }

    "modEnhancedblockentitiesClientAuto"(libs.enhancedblockentities)

    "modSodiumClientAuto"(libs.sodium)
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
        )
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
        extendsFrom(modClientAuto)
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
