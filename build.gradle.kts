// Not sure why this is here
//buildscript {
//    dependencies {
//        classpath("com.github.CDAGaming.CrowdinTranslate:crowdin-translate:${project.crowdin_translate_version}")
//    }
//}

plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.modrinth.minotaur") version "1.2.1"
    id("de.guntram.mcmod.crowdin-translate") version "1.4+1.18.2"
    `maven-publish`
}

version = properties["paradise_lost_version"]!!
group = "com.aether" //TODO: change this

repositories {
    mavenCentral()

    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }

    maven {
        name = "Guntram"
        url = uri("https://minecraft.guntram.de/maven/")
    }

    maven {
        name = "Modmenu Legacy"
        url = uri("https://maven.fabricmc.net/io/github/prospector/modmenu/")
    }

    maven {
        name = "Hephaestus"
        url = uri("https://hephaestus.dev/release")
    }

    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/releases/")
    }

    maven {
        name = "Ladysnake"
        url = uri("https://ladysnake.jfrog.io/artifactory/mods")
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
        name = "Gudenau"
        url = uri("https://maven.gudenau.net")
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
            version = properties["minecraft_version"].toString(),
    )

    mappings(
            group = "net.fabricmc",
            name = "yarn",
            version = "${properties["yarn_version"]}",
            classifier = "v2",
    )

    modImplementation(
            group = "net.fabricmc",
            name = "fabric-loader",
            version = properties["loader_version"].toString(),
    )

    modImplementation(
            group = "com.jamieswhiteshirt",
            name = "reach-entity-attributes",
            version = properties["entity_attributes_version"].toString(),
    ).also(::include)

    modImplementation(
            group = "com.github.CDAGaming.CrowdinTranslate",
            name = "crowdin-translate",
            version = properties["crowdin_translate_version"].toString(),
    ).also(::include)

    modImplementation(
            group = "dev.onyxstudios.cardinal-components-api",
            name = "cardinal-components-base",
            version = properties["cardinal_components_version"].toString(),
    ).also(::include)

    modImplementation(
            group = "dev.onyxstudios.cardinal-components-api",
            name = "cardinal-components-entity",
            version = properties["cardinal_components_version"].toString(),
    ).also(::include)

    modImplementation(
            group = "com.github.devs-immortal",
            name = "Incubus-Core",
            version = properties["incubus_core_version"].toString(),
    ).also(::include)

    modImplementation(
            group = "net.fabricmc.fabric-api",
            name = "fabric-api",
            version = properties["fabric_api_version"].toString(),
    )

    modImplementation(
            group = "net.kyrptonaught",
            name = "customportalapi",
            version = properties["customportalapi_version"].toString(),
    ).also(::include)

    modImplementation(
            group = "dev.emi",
            name = "trinkets",
            version = properties["trinkets_version"].toString(),
    ).also(::include)

    modImplementation(
            group = "net.gudenau.minecraft",
            name = "MoreTags",
            version = properties["moretags_version"].toString(),
    ).also(::include)

    modImplementation(
            group = "net.gudenau.minecraft",
            name = "RecipeConfidence",
            version = properties["recipeconfidence_version"].toString(),
    ).also(::include)

    modImplementation(
            group = "io.github.ladysnake",
            name = "satin",
            version = properties["satin_version"].toString(),
    ).also(::include)

    modRuntimeOnly(
            group = "com.terraformersmc",
            name = "modmenu",
            version = properties["modmenu_version"].toString(),
    )

    modRuntimeOnly(
            group = "me.shedaniel",
            name = "RoughlyEnoughItems-fabric",
            version = properties["rei_version"].toString(),
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
                    "Implementation-Version" to project.version,
                    "Main-Class" to "com.aether.executable.InstallerGUI",
            )
        }

        from("LICENSE.md")
    }

    withType<JavaCompile> {
        options.compilerArgs.add("-Xmaxerrs400")
    }
}

base {
    archivesName.set("paradise-lost")
}

java {
    sourceCompatibility = JavaVersion.toVersion(properties["java_version"]!!)
    withSourcesJar()
}

loom {
    accessWidenerPath.set(file("src/main/resources/the_aether.accesswidener"))

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

//task publishModrinth (type: TaskModrinthUpload){
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
    minecraftProjectName = "the_aether"
    verbose = true
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
