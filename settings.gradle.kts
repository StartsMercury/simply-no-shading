pluginManagement {
    repositories {
        // prefer to retrieve fabric loom plugin from here
        maven {
            name = "Fabric Maven"
            url = uri("https://maven.fabricmc.net")
        }

        // normal non-fabric plugins
        gradlePluginPortal()

        // for plugin dependency retrieval
        mavenCentral()
    }
}
