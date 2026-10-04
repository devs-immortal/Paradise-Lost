plugins {
    id("project-setup")

    alias(libs.plugins.moddevgradle)
}

val modId: String by project
val sherdsApiVersion: String by project

neoForge {
    neoFormVersion = libs.versions.neoform.get()

    file("src/main/resources/META-INF/accesstransformer.cfg").takeIf { it.exists() }?.let {
        accessTransformers.files.setFrom(it.path)
        validateAccessTransformers = true
    }

    parchment.minecraftVersion.set(libs.versions.parchment.minecraft.get())
    parchment.mappingsVersion.set(libs.versions.parchment.asProvider().get())
}

dependencies {
    compileOnly(libs.mixin)
    compileOnly(libs.mixinextras.common)
    compileOnly(libs.jspecify)
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("org.ow2.asm:asm:9.3")
    compileOnly("org.ow2.asm:asm-tree:9.3")

    // Sherds API — the shared half of the declaration (see the "Common" section of
    // https://github.com/thomasglasser/Sherds-API/wiki). This pins the artifact and version only:
    // nothing here is put on a loader's classpath, because project-setup copies :common's *sources*
    // into fabric/neoforge — never its dependencies, and never its embed configuration. Each loader
    // therefore also names its own loader-specific artifact (see fabric/build.gradle.kts and
    // neoforge/build.gradle.kts) to get it onto the dev classpath and nested into the output jar.
    // Leaving that out drops sherdsapi:stack_pot_decorations on the floor, which silently discards
    // already-persisted sherd stacks on world load.
    implementation("dev.thomasglasser.sherdsapi:sherdsapi-common-1.21.3:$sherdsApiVersion")
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn(tasks.named("commonRegSourcesJar"))
}

sourceSets.named("main") {
    java.srcDir(layout.buildDirectory.dir("tmp/commonRegSourcesJar/sources"))
    resources.srcDir(file("src/generated/resources"))
}

//<editor-fold defaultstate="collapsed" desc="<Publishing>">
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
//</editor-fold>