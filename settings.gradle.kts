pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Jigpack"
            url = uri("https://jitpack.io")
        }
        maven {
            name = "Guntram"
            url = uri("https://minecraft.guntram.de/maven/")
        }
    }
}
