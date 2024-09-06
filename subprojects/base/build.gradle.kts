repositories {
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com")
        content {
            includeModule("com.terraformersmc", "modmenu")
        }
    }

    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    // BedrockIfy
    modCompileOnly(group = "maven.modrinth", name = "bedrockify", version = "1.10.1+mc1.21")

    // Enhanced Block Entities
    modCompileOnly(group = "maven.modrinth", name = "ebe", version = "0.10.1+1.21")

    // Fabric API
    modCompileOnly(fabricApi.module("fabric-api", "0.103.0+1.21.1"))

    // Mod Menu
    modCompileOnly(group = "com.terraformersmc", name = "modmenu", version = "11.0.2")

    // Sodium
    modCompileOnly(group = "maven.modrinth", name = "sodium", version = "mc1.21-0.6.0-beta.1-fabric")
}

testing {
    @Suppress("UnstableApiUsage")
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}
