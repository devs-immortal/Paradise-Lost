import net.darkhax.curseforgegradle.TaskPublishCurseForge

plugins {
    id("project-setup")

    alias(libs.plugins.minotaur)
    alias(libs.plugins.curseforgegradle)
    alias(libs.plugins.moddevgradle)
}

val modId: String by project
val sherdsApiVersion: String by project

val commonMainResources = rootProject.project(":common").layout.projectDirectory.dir("src/main/resources").asFile
val commonGeneratedResources = rootProject.project(":common").layout.projectDirectory.dir("src/generated/resources").asFile

neoForge {
    version = libs.versions.neoforge.asProvider().get()

    project(":common").file("src/main/resources/META-INF/accesstransformer.cfg").takeIf { it.exists() }?.let {
        accessTransformers.files.setFrom(it)
        validateAccessTransformers = true
    }

    parchment.minecraftVersion.set(libs.versions.parchment.minecraft.get())
    parchment.mappingsVersion.set(libs.versions.parchment.asProvider().get())

    // Must be a sibling of runs (not nested inside it) so the mod classes+resources
    // are on the game module path for client/server/data.
    mods {
        create(modId) {
            sourceSet(project.sourceSets.main.get())
        }
    }

    runs {
        configureEach {
            logLevel = org.slf4j.event.Level.DEBUG
            // Match Warden Tools naming so IDEA run configs are easy to find
            ideName = "NeoForge ${name.replaceFirstChar { it.uppercase() }} (${project.path})"
        }

        create("client") {
            client()
        }

        create("server") {
            server()
            programArgument("--nogui")
        }

        // Warden Tools pattern: NeoForge datagen writes into common/src/generated/resources
        create("data") {
            data()
            programArguments.addAll(
                "--mod", modId,
                "--all",
                "--output", commonGeneratedResources.absolutePath,
                "--existing", commonMainResources.absolutePath
            )
        }
    }
}


repositories {
    maven {
        // location of the maven that hosts JEI files since January 2023
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
    }
}


dependencies {
    // Do NOT compileOnly(project(":common")) — common's MDG deps (fml_loader) would land on
    // IDEA's NeoForge module path and cause "reads more than one module named fml_loader".
    // Common sources/resources are already merged via project-setup (WT commonJava pattern).
    implementation(libs.jspecify)
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    // project-setup also compiles :common's sources into this module, so it needs the same mixin
    // APIs :common has. MixinExtras itself is bundled with NeoForge at runtime.
    compileOnly(libs.mixinextras.common)

    // Sherds API: `implementation` puts it on the dev runtime classpath so FML loads it as a mod in
    // runs (matching production), `jarJar` nests it — plus its TommyLib transitive — into the jar.
    // Both are needed, and neither can live in :common, which project-setup only copies *sources*
    // from. Version comes from gradle.properties; the shared artifact coordinate is declared in
    // :common/build.gradle.kts.
    jarJar(implementation("dev.thomasglasser.sherdsapi:sherdsapi-neoforge-1.21.1:$sherdsApiVersion")!!)

    runtimeOnly("mezz.jei:jei-1.21.1-neoforge:19.57.0.449")

    // Mod Dependencies below
    //implementation(libs.geckolib.neoforge)
}

// Compile NeoForge RegistrationUtils sources into this mod (AutoService factory on classpath for runs).
sourceSets.named("main") {
    java.srcDir(layout.buildDirectory.dir("tmp/neoforgeRegSourcesJar/sources"))
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn("neoforgeRegSourcesJar")
}

// Shade RegistrationUtils into the mod jar without adding the joined GAMELIBRARY to runtimeClasspath.
afterEvaluate {
    val reg = extensions.getByName("reg") as com.matyrobbrt.registrationutils.gradle.RegExtension
    reg.configureJarTask(tasks.named<Jar>("jar").get())
}

// Do not package Fabric-only metadata into the NeoForge mod.
tasks.withType<ProcessResources>().configureEach {
    exclude("fabric.mod.json")
    exclude("**/*.accesswidener")
    exclude("**/*.classtweaker")
}

//<editor-fold defaultstate="collapsed" desc="<Publishing>">
modrinth {
    token = System.getenv("MODRINTH_TOKEN") ?: "Invalid/No API Token Found"
    uploadFile.set(tasks.named<Jar>("jar"))
    projectId.set(properties["modrinthProjectId"] as String)
    versionName = "NeoForge ${libs.versions.minecraft.asProvider().get()}"
    versionType = "release"
    loaders.set(listOf("neoforge"))
    versionNumber.set(project.version.toString())
    gameVersions.set(listOf(libs.versions.minecraft.asProvider().get()))

    if (rootProject.file("CHANGELOG.md").exists())
        changelog = rootProject.file("CHANGELOG.md").readText(Charsets.UTF_8)

    // Comment out below to enable publishing properly
    debugMode = true
    // See below for other properties and info
    // https://github.com/modrinth/minotaur#available-properties
}

tasks.register<TaskPublishCurseForge>("publishToCurseForge") {
    group = "publishing"
    apiToken = System.getenv("CURSEFORGE_TOKEN") ?: "Invalid/No API Token Found"

    val mainFile = upload(properties["curseforgeProjectId"], tasks.jar)
    mainFile.displayName = "${properties["modDisplayName"]} NeoForge ${libs.versions.minecraft.asProvider().get()} ${project.version}"
    mainFile.releaseType = "release"
    mainFile.addModLoader("NeoForge")
    mainFile.addGameVersion(libs.versions.minecraft.asProvider().get())
    mainFile.addJavaVersion("Java ${libs.versions.java.get()}")
    mainFile.addEnvironment("Client", "Server")

    if (rootProject.file("CHANGELOG.md").exists()) {
        mainFile.changelog = rootProject.file("CHANGELOG.md").readText(Charsets.UTF_8)
        mainFile.changelogType = "markdown"
    }

    // Comment out below to enable publishing properly
    debugMode = true
    // See below for other properties and info
    // https://github.com/Darkhax/CurseForgeGradle#available-properties
}

publishing {
    publishing {
        publications {
            create<MavenPublication>(modId) {
                from(components["java"])
                artifactId = base.archivesName.get()
            }
        }
    }
}

tasks.named<DefaultTask>("publish") {
    finalizedBy("modrinth")
    finalizedBy("publishToCurseForge")
}
//</editor-fold>