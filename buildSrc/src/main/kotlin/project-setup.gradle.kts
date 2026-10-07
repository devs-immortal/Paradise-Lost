plugins {
    java
    `maven-publish`
    idea
    eclipse
    checkstyle
    id("repositories")
}

val libs = project.versionCatalogs.find("libs")
version = getVersion("version")

java {
    toolchain.languageVersion = JavaLanguageVersion.of(getVersion("java"))

    withSourcesJar()
    // Enable if you also want to generate a javadoc jar
    //withJavadocJar()
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

eclipse {
    classpath {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

tasks.withType<JavaCompile>().configureEach {
    this.options.encoding = "UTF-8"
    this.options.release.set(java.toolchain.languageVersion.get().asInt())
}

val modId               : String by project
val modDisplayName      : String by project
val modAuthors          : String by project
val modLicense          : String by project
val modDescription      : String by project
val modHomepage         : String by project
val modIssuesTracker    : String by project
val modGitRepo          : String by project

base {
    archivesName = "$modId-${project.name}-${getVersion("minecraft")}"
}

checkstyle {
    sourceSets = emptyList()
}

tasks.named<Checkstyle>("checkstyleMain") {
    setSource(layout.projectDirectory.dir("src/main/java"))
}

tasks.withType<Test>().configureEach {
    failOnNoDiscoveredTests = false
}

tasks.named<JavaCompile>("compileJava") {
    if (project != project(":common")) {
        // Ensure generated RegistrationUtils sources exist before compiling loaders.
        dependsOn(project(":common").tasks.named("commonRegSourcesJar"))
        source(project(":common").sourceSets.main.get().allSource)
    }
}

// MDG may reconfigure compileJava after plugin apply; pin common sources onto the source set
// so they stay part of the NeoForge/Fabric mod classpath used by runs.
afterEvaluate {
    if (project != project(":common")) {
        sourceSets.named("main") {
            val commonJava = project(":common").sourceSets.main.get().java
            commonJava.sourceDirectories.files.forEach { dir ->
                java.srcDir(dir)
            }
        }
        tasks.named<JavaCompile>("compileJava") {
            dependsOn(project(":common").tasks.named("commonRegSourcesJar"))
            source(project(":common").sourceSets.main.get().allSource)
        }
    }
}

tasks.named<Jar>("sourcesJar") {
    if (project != project(":common"))
        from(project(":common").sourceSets.main.get().allSource)
}

tasks.withType<Javadoc>().configureEach {
    if (project != project(":common"))
        source(project(":common").sourceSets.main.get().allJava)
}

tasks.withType<ProcessResources>().configureEach {
    if (project != project(":common"))
        from(project(":common").sourceSets.main.get().resources)

    // Loader metadata must live in the loader project only.
    // Common may ship shared assets/data, mixins.json, pack.mcmeta, and AT/AW.
    if (project.name == "neoforge") {
        exclude("fabric.mod.json")
        exclude("**/*.accesswidener")
        exclude("**/*.classtweaker")
    }
    if (project.name == "fabric") {
        exclude("**/accesstransformer.cfg")
        exclude("**/neoforge.mods.toml")
    }

    val expandProps = mapOf(
        "group"                          to project.group,
        "mod_id"                         to modId,
        "mod_display_name"               to modDisplayName,
        "mod_license"                    to modLicense,
        "mod_authors"                    to modAuthors,
        "mod_description"                to modDescription,
        "mod_homepage"                   to modHomepage,
        "mod_issues_tracker"             to modIssuesTracker,
        "mod_git_repo"                   to modGitRepo,

        "version"                        to getVersion("version"),
        "java_version"                   to getVersion("java"),

        "minecraft_version"              to getVersion("minecraft"),
        "minecraft_version_range"        to getVersion("minecraft.range"),

        "fabric_api_version"             to getVersion("fabric.api"),
        "fabric_api_version_range"       to getVersion("fabric.api.range"),
        "fabric_loader_version"          to getVersion("fabric"),
        "fabric_loader_version_range"    to getVersion("fabric.range"),

        "neoforge_version"               to getVersion("neoforge"),
        "neoforge_version_range"         to getVersion("neoforge.range"),
        "neoforge_loader_version_range"  to getVersion("neoforge.loader.range")
    )

    val strategy = duplicatesStrategy
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/neoforge.mods.toml")) {
        expand(expandProps)
    }

    duplicatesStrategy = strategy

    inputs.properties(expandProps)
}

tasks.withType<Jar>().configureEach {
    // Two separate things feed duplicate paths into these archives:
    //
    //  1. sourcesJar bundles sourceSets.main.allSource, and :common's `main` has both
    //     src/main/resources and datagen's src/generated/resources registered as resource dirs
    //     (see common/build.gradle.kts). ~146 assets currently exist in both.
    //  2. The loader projects (fabric/neoforge) receive RegistrationUtils twice: compiled from the
    //     relocated loader sources pinned onto `main` -- where the AutoService processor emits
    //     META-INF/services/net.id.paradise_lost.registration.* -- *and* shaded in by
    //     `reg.configureJarTask(jar)` from the relocated RegistrationUtils jars. Both provide
    //     net/id/paradise_lost/registration/**.
    //
    // In every case the copies hold the same content, and a jar may only carry one entry per path
    // regardless -- two META-INF/services files for one service means ServiceLoader only ever reads
    // one of them. So keep the first copy rather than failing the build.
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes(mapOf(
            "Specification-Title"     to modDisplayName,
            "Specification-Vendor"    to modAuthors,
            "Specification-Version"   to getVersion("version"),
            "Implementation-Title"    to modDisplayName,
            "Implementation-Version"  to getVersion("version"),
            "Implementation-Vendor"   to modAuthors,
            "Built-On-Minecraft"      to getVersion("minecraft"),
            "MixinConfigs"            to "$modId.mixins.json"
        ))
    }
}

// Must have your maven host login username and password in your system's environment variables (see below references)
// Read more about environment variables here: https://www.howtogeek.com/787217/how-to-edit-environment-variables-on-windows-10-or-11/
// Don't forget to replace the maven URL below
// If your project is OSS, consider using Cloudsmith as your maven host: https://help.cloudsmith.io/docs/open-source-hosting-policy
publishing {
    repositories {
        if (System.getenv("MAVEN_USERNAME") == null && System.getenv("MAVEN_PASSWORD") == null) {
            mavenLocal()
        }
        else maven {
            name = "Maven"
            url = uri("https://maven.cloudsmith.io/myname/mymod/")

            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

gradle.projectsEvaluated {
    tasks.withType(JavaCompile::class) {
        options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "1000"))
    }
}

fun getVersion(versionName: String): String {
    return libs.get().findVersion(versionName).get().requiredVersion
}