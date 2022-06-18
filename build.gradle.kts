// Not sure why this is here
//buildscript {
//    dependencies {
//        classpath("com.github.CDAGaming.CrowdinTranslate:crowdin-translate:${project.crowdin_translate_version}")
//    }
//}

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
val moreTagsVersion: String by properties
val recipeConfidenceVersion: String by properties
val satinVersion: String by properties

plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.modrinth.minotaur") version "1.2.1"
    id("de.guntram.mcmod.crowdin-translate") version "1.4+1.18.2"
    `maven-publish`
}

version = paradiseLostVersion
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

val includeModImplementation: Configuration by configurations.creating

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

    includeModImplementation(
            group = "dev.onyxstudios.cardinal-components-api",
            name = "cardinal-components-base",
            version = cardinalComponentsVersion,
    )

    includeModImplementation(
            group = "dev.onyxstudios.cardinal-components-api",
            name = "cardinal-components-entity",
            version = cardinalComponentsVersion,
    )

    includeModImplementation(
            group = "com.github.CDAGaming.CrowdinTranslate",
            name = "crowdin-translate",
            version = crowdinTranslateVersion,
    )

    includeModImplementation(
            group = "net.kyrptonaught",
            name = "customportalapi",
            version = customportalapiVersion,
    )

    modImplementation(
            group = "net.fabricmc.fabric-api",
            name = "fabric-api",
            version = fabricApiVersion,
    )

    includeModImplementation(
            group = "com.github.devs-immortal",
            name = "Incubus-Core",
            version = incubusCoreVersion,
    )

    includeModImplementation(
            group = "net.gudenau.minecraft",
            name = "MoreTags",
            version = moreTagsVersion,
    )

    includeModImplementation(
            group = "com.jamieswhiteshirt",
            name = "reach-entity-attributes",
            version = entityAttributesVersion,
    )

    includeModImplementation(
            group = "net.gudenau.minecraft",
            name = "RecipeConfidence",
            version = recipeConfidenceVersion,
    )

    includeModImplementation(
            group = "io.github.ladysnake",
            name = "satin",
            version = satinVersion,
    )

    includeModImplementation(
            group = "dev.emi",
            name = "trinkets",
            version = trinketsVersion,
    )

    modRuntimeOnly(
            group = "com.terraformersmc",
            name = "modmenu",
            version = modmenuVersion,
    )

    modRuntimeOnly(
            group = "me.shedaniel",
            name = "RoughlyEnoughItems-fabric",
            version = reiVersion,
    )
}

configurations {
    include.get().extendsFrom(includeModImplementation)
    modImplementation.get().extendsFrom(includeModImplementation)
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
                    "Main-Class" to "com.aether.executable.InstallerGUI",
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
