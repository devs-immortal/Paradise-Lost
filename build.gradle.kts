val paradiseLostVersion: String by properties

val minecraftVersion: String by properties
val yarnVersion: String by properties
val loaderVersion: String by properties
val javaVersion: String by properties

val fabricApiVersion: String by properties
val incubusCoreVersion: String by properties
val customportalapiVersion: String by properties
val cardinalComponentsVersion: String by properties
val trinketsVersion: String by properties
val crowdinTranslateVersion: String by properties
val entityAttributesVersion: String by properties
val modmenuVersion: String by properties
val reiVersion: String by properties
val satinVersion: String by properties
val clothConfigVersion: String by properties

plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.modrinth.minotaur") version "1.2.1"
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
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }

    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }

    maven {
        name = "Ladysnake Mods"
        url = uri("https://maven.ladysnake.org/releases")
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
            version = yarnVersion,
            classifier = "v2",
    )

    modImplementation(
            group = "net.fabricmc",
            name = "fabric-loader",
            version = loaderVersion,
    )

    modImplementation(
            group = "dev.onyxstudios.cardinal-components-api",
            name = "cardinal-components-base",
            version = cardinalComponentsVersion,
    ).also(::include)

    modImplementation(
            group = "dev.onyxstudios.cardinal-components-api",
            name = "cardinal-components-entity",
            version = cardinalComponentsVersion,
    ).also(::include)

    modImplementation(
            group = "net.kyrptonaught",
            name = "customportalapi",
            version = customportalapiVersion,
    ).also(::include)

    modImplementation(
            group = "net.fabricmc.fabric-api",
            name = "fabric-api",
            version = fabricApiVersion,
    )

    modImplementation(
            group = "com.github.devs-immortal",
            name = "Incubus-Core",
            version = incubusCoreVersion,
    ).also(::include)

    modImplementation(
            group = "com.jamieswhiteshirt",
            name = "reach-entity-attributes",
            version = entityAttributesVersion,
    ).also(::include)

    modImplementation(
            group = "io.github.ladysnake",
            name = "satin",
            version = satinVersion,
    ).also(::include)

    modImplementation(
            group = "dev.emi",
            name = "trinkets",
            version = trinketsVersion,
    ).also(::include)

    modImplementation(
            group = "me.shedaniel.cloth",
            name = "cloth-config-fabric",
            version = clothConfigVersion,
    )
    modImplementation(
            group = "dev.onyxstudios.cardinal-components-api",
            name = "cardinal-components-base",
            version = cardinalComponentsVersion,
    )

    modRuntimeOnly(
            group = "com.terraformersmc",
            name = "modmenu",
            version = modmenuVersion,
    )
    // Adds a dependency on the base cardinal components module (required by every other module)
    // Replace modImplementation with modApi if you expose components in your own API

//    modRuntimeOnly(
//            group = "me.shedaniel",
//            name = "RoughlyEnoughItems-fabric",
//            version = reiVersion,
//    )

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
    setCrowdinProjectname("aether")
    minecraftProjectName = "paradise_lost"
    verbose = true
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
