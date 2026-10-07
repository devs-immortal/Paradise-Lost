// Add your dependency repositories here
repositories {
    mavenRepo("ParchmentMC",
        "https://maven.parchmentmc.org",
        "org.parchmentmc", "org.parchmentmc.data")

    mavenRepo("Fabric",
        "https://maven.fabricmc.net/",
        "net.fabricmc", "net.fabricmc.fabric-api")

    mavenRepo("Ladysnake",
        "https://maven.ladysnake.org/releases",
        "org.ladysnake.cardinal-components-api")

    mavenRepo("JitPack",
        "https://jitpack.io",
        "com.github.Chocohead")

    mavenRepo("ThomasGlasser",
        "https://maven.thomasglasser.dev/releases",
        "dev.thomasglasser.sherdsapi",
        "dev.thomasglasser.tommylib")

    mavenRepo("Modrinth",
        "https://api.modrinth.com/maven",
        "maven.modrinth")
}

/**
 * Standard Maven repository
 *
 * @param name The display name of the repository. Only used for logging
 * @param uri The maven repository URL
 * @param groups The artifact group identifiers for the repository
 */
fun RepositoryHandler.mavenRepo(name: String, uri: String, vararg groups: String) {
    exclusiveContent {
        forRepository {
            maven {
                this.name = name
                this.url = uri(uri)
            }
        }
        filter {
            for (group in groups) {
                includeGroup(group)
            }
        }
    }
}

/**
 * Flat-directory repository, acting as a folder in your project root directory for pre-compiled binaries
 *
 * @param folderName The path of the folder to use, relative to the project root. E.G. "libs"
 */
fun RepositoryHandler.folder(folderName: String) {
    flatDir {
        dirs("$projectDir/$folderName")
    }
}

/**
 * {@link https://central.sonatype.com MavenCentral}-based repository,
 * taking the artifact groups as the identifier
 *
 * @param groups The artifact group identifier for each MavenCentral artifact
 */
fun RepositoryHandler.mavenCentral(vararg groups: String) {
    exclusiveContent {
        forRepository {
            mavenCentral()
        }
        filter {
            for (group in groups) {
                includeGroup(group)
            }
        }
    }
}
