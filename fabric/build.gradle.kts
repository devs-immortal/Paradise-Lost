import net.darkhax.curseforgegradle.Constants
import net.fabricmc.loom.task.RemapJarTask
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.gradle.internal.extensions.stdlib.capitalized

plugins {
    id("project-setup")

    alias(libs.plugins.minotaur)
    alias(libs.plugins.curseforgegradle)
    alias(libs.plugins.loom)
}

val modId: String by project
val sherdsApiVersion: String by project
val wilderWildVersion = "4.2.1-mc1.21.1"
val frozenLibVersion = "2.2.4-mc1.21.1"

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered() {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchment.minecraft.get()}:${libs.versions.parchment.asProvider().get()}@zip")
    })
    modImplementation(libs.fabric)
    modImplementation(libs.fabric.api)
    implementation(libs.jspecify)
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    // project-setup also compiles :common's sources into this module, so it needs the same mixin
    // APIs :common has. MixinExtras itself is bundled with Fabric Loader at runtime.
    compileOnly(libs.mixinextras.common)

    // Sherds API: `include` nests it into the remapped jar, `modImplementation` puts the remapped
    // artifact on the dev classpath as a mod. Both are needed — dev and production must agree — and
    // neither can live in :common, which project-setup only copies *sources* from. Version comes
    // from gradle.properties; the shared artifact coordinate is declared in :common/build.gradle.kts.
    modImplementation(include("dev.thomasglasser.sherdsapi:sherdsapi-fabric-1.21.1:$sherdsApiVersion")!!)

    // Common sources/resources come from project-setup; avoid project(:common) so MDG/Loom
    // transitive game libs are not duplicated onto the loader classpath.
}

// Compile Fabric RegistrationUtils sources (loader factory) into this mod.
sourceSets.named("main") {
    java.srcDir(layout.buildDirectory.dir("tmp/fabricRegSourcesJar/sources"))
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn("fabricRegSourcesJar")
}

// Shade RegistrationUtils into the jar (remapJar will pick it up); do not add joined GAMELIBRARY.
afterEvaluate {
    val reg = extensions.getByName("reg") as com.matyrobbrt.registrationutils.gradle.RegExtension
    reg.configureJarTask(tasks.named<Jar>("jar").get())
}

sourceSets {
    create("testmod") {
        val main = sourceSets["main"]
        java.srcDir(project(":common").file("src/testmod/java"))
        resources.srcDir(project(":common").file("src/testmod/resources"))
        compileClasspath += main.compileClasspath + main.output
        runtimeClasspath += main.runtimeClasspath + main.output
    }
}

sourceSets {
    create("compat") {
        val testmod = sourceSets["testmod"]
        compileClasspath += testmod.compileClasspath
        runtimeClasspath += testmod.runtimeClasspath + testmod.output
    }
}

loom.createRemapConfigurations(sourceSets["compat"])

val modCompatRuntimeOnly by configurations.getting

dependencies {
    modCompileOnly("maven.modrinth:wilder-wild:$wilderWildVersion")
    modCompileOnly("maven.modrinth:frozenlib:$frozenLibVersion")
    modCompatRuntimeOnly("maven.modrinth:wilder-wild:$wilderWildVersion")
    modCompatRuntimeOnly("maven.modrinth:frozenlib:$frozenLibVersion")
}

tasks.named("check") {
    dependsOn("testmodClasses")
}

loom {
    listOf(
        file("src/main/resources/$modId.accesswidener"),
        project(":common").file("src/main/resources/$modId.accesswidener"),
        file("src/main/resources/$modId.classtweaker")
    ).firstOrNull { it.exists() }?.let(accessWidenerPath::set)

    mods {
        create(modId) {
            sourceSet(sourceSets["main"])
        }
        create("${modId}_testmod") {
            sourceSet(sourceSets["testmod"])
        }
    }

    runs {
        configureEach {
            runDir("runs/$name")
            ideConfigGenerated(true)
            configName = "Fabric ${name.capitalized()}"
        }

        named("client") {
            client()
            programArg("--username=Dev")
        }

        named("server") {
            server()
        }

        create("clientTest") {
            client()
            configName = "Fabric Client Game Tests"
            source(sourceSets["testmod"])
            runDir("runs/clienttest")
            programArg("--username=Tester")
        }

        create("clientCompatTest") {
            client()
            configName = "Fabric Client Compat Tests"
            source(sourceSets["compat"])
            runDir("runs/compattest")
            programArg("--username=Tester")
            environmentVariable("RUN_ONLY", "Compat")
        }

        create("serverTest") {
            server()
            configName = "Fabric Server Tests"
            source(sourceSets["testmod"])
            runDir("runs/servertest")
            programArg("--nogui")
        }
    }
}

val prepareServerTest by tasks.registering {
    val dir = file("runs/servertest")
    doLast {
        dir.resolve("world").deleteRecursively()
        dir.mkdirs()
        dir.resolve("eula.txt").writeText("eula=true\n")
        dir.resolve("server.properties").writeText(
            """
            online-mode=false
            level-type=minecraft:flat
            difficulty=peaceful
            generator-settings={"layers":[{"block":"minecraft:bedrock","height":1},{"block":"minecraft:dirt","height":2},{"block":"minecraft:grass_block","height":1}],"biome":"minecraft:plains","structure_overrides":[]}
            max-tick-time=-1
            """.trimIndent() + "\n"
        )
    }
}

tasks.matching { it.name == "runServerTest" }.configureEach {
    dependsOn(prepareServerTest)
}

tasks.withType<ProcessResources>().configureEach {
    // AT is NeoForge-only; keep AW from common via project-setup resource merge.
    exclude("**/accesstransformer.cfg")
    exclude("**/neoforge.mods.toml")
}

//<editor-fold defaultstate="collapsed" desc="<Publishing>">
// Must have your Modrinth API Key as an environment variable under 'MODRINTH_TOKEN'
modrinth {
    token = System.getenv("MODRINTH_TOKEN") ?: "Invalid/No API Token Found"
    uploadFile.set(tasks.named<RemapJarTask>("remapJar"))
    projectId.set(properties["modrinthProjectId"] as String)
    versionName = "Fabric ${libs.versions.minecraft.asProvider().get()}"
    versionType = "release"
    loaders.set(listOf("fabric"))
    versionNumber.set(project.version.toString())
    gameVersions.set(listOf(libs.versions.minecraft.asProvider().get()))
    dependencies {
        required.project("fabric-api")
    }

    if (rootProject.file("CHANGELOG.md").exists())
        changelog.set(rootProject.file("CHANGELOG.md").readText(Charsets.UTF_8))

    // Comment out below to enable publishing properly
    debugMode = true
    // See below for other properties and info
    // https://github.com/modrinth/minotaur#available-properties
}

// Must have your CurseForge API Key as an environment variable under 'CURSEFORGE_TOKEN'
tasks.register<TaskPublishCurseForge>("publishToCurseForge") {
    group = "publishing"
    apiToken = System.getenv("CURSEFORGE_TOKEN") ?: "Invalid/No API Token Found"

    val mainFile = upload(properties["curseforgeProjectId"], tasks.remapJar)
    mainFile.displayName = "${properties["modDisplayName"]} Fabric ${libs.versions.minecraft.asProvider().get()} ${project.version}"
    mainFile.releaseType = "release"
    mainFile.addModLoader("Fabric")
    mainFile.addGameVersion(libs.versions.minecraft.asProvider().get())
    mainFile.addJavaVersion("Java ${libs.versions.java.get()}")
    mainFile.addRelation("fabric-api", Constants.RELATION_REQUIRED)
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
    publications {
        create<MavenPublication>(modId) {
            from(components["java"])
            artifactId = base.archivesName.get()
        }
    }
}

tasks.named<DefaultTask>("publish") {
    finalizedBy("modrinth")
    finalizedBy("publishToCurseForge")
}
//</editor-fold>
