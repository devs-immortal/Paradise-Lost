val paradiseLostVersion: String by properties

val minecraftVersion: String by properties
val yarnMappings: String by properties
val loaderVersion: String by properties
val javaVersion: String by properties

val fabricVersion: String by properties
val fabricAsmVersion: String by properties
val sherdsApiVersion: String by properties
val customPortalApiVersion: String by properties
val cardinalComponentsVersion: String by properties

val moonlightVersion: String by properties
val everyCompatVersion: String by properties
val stoneZoneVersion: String by properties

plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.8.7"
    id("de.guntram.mcmod.crowdin-translate") version "1.4+1.19"
    `maven-publish`
    checkstyle
}

version = paradiseLostVersion
group = "net.id"

repositories {
    mavenCentral()

    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }

    maven {
        name = "Hephaestus"
        url = uri("https://hephaestus.dev/release")
    }

    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/")
    }

    maven {
        name = "Ladysnake Mods"
        url = uri("https://maven.ladysnake.org/releases")
    }

    maven {
        name = "Shedaniel"
        url = uri("https://maven.shedaniel.me/")
    }

    maven {
        name = "Kyrptonaught"
        url = uri("https://maven.kyrptonaught.dev/")
    }

    maven {
        name = "JamesWhiteShirt"
        url = uri("https://maven.jamieswhiteshirt.com/libs-release/")
    }

    maven {
        name = "Sherds API"
        url = uri("https://dl.cloudsmith.io/public/thomasglasser/sherdsapi/maven/")
    }

    maven {
        name = "Sherds API (Tommylib)"
        url = uri("https://dl.cloudsmith.io/public/thomasglasser/tommylib/maven/")
    }

    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }

    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    minecraft(
            group = "com.mojang",
            name = "minecraft",
            version = minecraftVersion,
    )

    mappings(
            group = "net.fabricmc",
            name = "yarn",
            version = yarnMappings,
            classifier = "v2",
    )

    modImplementation(
            group = "net.fabricmc",
            name = "fabric-loader",
            version = loaderVersion,
    )

    modImplementation(
            group = "org.ladysnake.cardinal-components-api",
            name = "cardinal-components-base",
            version = cardinalComponentsVersion,
    ).also(::include)

    modImplementation(
            group = "org.ladysnake.cardinal-components-api",
            name = "cardinal-components-entity",
            version = cardinalComponentsVersion,
    ).also(::include)

    modImplementation(
            group = "net.kyrptonaught",
            name = "customportalapi",
            version = customPortalApiVersion,
    ).also(::include).exclude(module = "sodium")

    modImplementation(
            group = "com.github.Chocohead",
            name = "Fabric-ASM",
            version = fabricAsmVersion,
    ).also(::include)

    modImplementation(
            group = "dev.thomasglasser.sherdsapi",
            name = "sherdsapi-fabric-1.21.1",
            version = sherdsApiVersion,
    ).also(::include)

    modImplementation(
            group = "net.fabricmc.fabric-api",
            name = "fabric-api",
            version = fabricVersion,
    )

    modImplementation(
            group = "maven.modrinth",
            name = "moonlight",
            version = moonlightVersion,
    )

    modImplementation(
            group = "maven.modrinth",
            name = "every-compat",
            version = everyCompatVersion,
    )

    modImplementation(
            group = "maven.modrinth",
            name = "stone-zone",
            version = stoneZoneVersion,
    )

}

tasks {
    processResources {
        inputs.property("version", version)

        filesMatching("fabric.mod.json") {
            expand("version" to version)
        }
    }

    build {
        dependsOn(downloadTranslations)
    }

    jar {
        manifest {
            attributes(
                    "Implementation-Title" to "ParadiseLost",
                    "Implementation-Version" to paradiseLostVersion,
                    "Main-Class" to "net.id.paradiselost.executable.InstallerGUI",
            )
        }

        from("LICENSE.md")
    }

    withType<JavaCompile> {
        options.compilerArgs.addAll(listOf("-Xmaxerrs", "400"))
    }
}

base {
    archivesName.set("paradise-lost")
}

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    withSourcesJar()
}

loom {
    accessWidenerPath.set(file("src/main/resources/paradise_lost.accesswidener"))

    runs {
        getByName("client") {
            programArg("--username=${System.getProperty("user.name")}")
            runDir = "run/client"
            client()
        }

        getByName("server") {
            runDir = "run/server"
            server()
        }

        create("clientDebug") {
            inherit(getByName("client"))
            configName = "Minecraft Client (Mixin Debug)"
            vmArgs.add("-Dmixin.debug.export=true")
        }

        create("serverDebug") {
            inherit(getByName("server"))
            vmArgs.add("-Dmixin.debug.export=true")
            configName = "Minecraft Server (Mixin Debug)"
        }
    }
}

//task publishModrinth (type: TaskModrinthUpload) {
//    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//    System.out.println('Enter the modrinth auth token: ');
//    token = br.readLine(); // Get password
//    projectId = 'IKpsG0nF'
//    System.out.println('Enter the version number:');
//    versionNumber = br.readLine();
//    System.out.println('Enter the version name:');
//    versionName = br.readLine();
//    uploadFile = jar // This is the java jar task
//    System.out.println('Enter the game version number: (See minotaur docs for valids)');
//    addGameVersion(br.readLine());
//    System.out.println('Enter changelog:');
//    changelog = br.readLine();
//    addLoader('fabric')
//}

crowdintranslate {
    setCrowdinProjectname("paradiselost")
    minecraftProjectName = "paradise_lost"
    verbose = true
}

fabricApi {
    configureDataGeneration()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

checkstyle {
    sourceSets = emptyList()
}
