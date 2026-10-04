import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.taskTriggers
import com.matyrobbrt.registrationutils.gradle.RegistrationUtilsExtension.SubProject.Type

plugins {
    alias(libs.plugins.moddevgradle) apply false
    alias(libs.plugins.loom) apply false

    alias(libs.plugins.minotaur) apply false
    alias(libs.plugins.curseforgegradle) apply false
    alias(libs.plugins.ideaext)

    id("com.matyrobbrt.mc.registrationutils") version "1.21.3-0.1.1"

    id("project-setup") apply false
    id("setup-refactoring")
}

registrationUtils {
    // Relocate away from project group net.id so packages are not shared with the mod module.
    group("net.id.paradise_lost.registration")
    // Avoid putting the joined GAMELIBRARY jar on runtimeClasspath (module clash with shaded jar).
    // Loader projects manually call reg.configureJarTask(jar) to shade instead.
    addDependencies(false)
    projects {
        register("fabric") { type.set(Type.FABRIC) }
        register("neoforge") { type.set(Type.NEOFORGE) }
        register("common") { type.set(Type.COMMON) }
    }
}

idea.project.settings.taskTriggers.beforeSync(tasks.getByName("refactorOnInitialSetup"))
