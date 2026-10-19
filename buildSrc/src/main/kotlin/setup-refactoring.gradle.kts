import org.gradle.internal.file.FileException
import java.nio.file.Files

val modId: String by project

/**
 * Create a task for refactoring the project when initially created, automatically handling
 * all package, file, and file contents renames to the new modId
 */
tasks.register("refactorOnInitialSetup", Action<Task> {
    group = "build setup"

    if (!isInitialUnmodifiedSetup())
        return@Action

    logger.lifecycle("Checking for initial setup refactor")

    if (modId == "mymod") {
        logger.lifecycle("Skipping setup refactor, modId hasn't been set in gradle.properties")

        return@Action
    }

    if (project.properties["group"] == "io.github.myname") {
        logger.lifecycle("Skipping setup refactor, group hasn't been set in gradle.properties")

        return@Action
    }

    logger.info("Performing setup refactor for modId: {}", modId)

    // Refactor main sources imports and mod entrypoint ModId declaration
    var result = true

    try {
        for (module in arrayOf("common", "fabric", "forge", "neoforge")) {
            result = refactorModule(module)

            if (!result)
                break
        }
    }
    catch (e: Exception) {
        logger.error("Failed to perform setup refactor", e)
        result = false
    }

    if (result) {
        // Cleanup old directories
        logger.info("Refactored project for new mod ID $modId, cleaning up")

        for (module in arrayOf("common", "fabric", "forge", "neoforge")) {
            val project = findProject(":$module") ?: continue

            var dir = project.projectDir.resolve("src/main/java/io/github/myname/mymod")

            delete(dir)

            dir = dir.parentFile

            while (dir.exists() && dir.isDirectory && dir.name != "java" && checkFileIsInProject(dir) && !Files.newDirectoryStream(dir.toPath()).use { stream -> stream.any()}) {
                delete(dir)
                dir = dir.parentFile
            }

            delete(project.projectDir.resolve("src/main/resources/META-INF/services/io.github.myname.mymod.platform.PlatformHelper").path)
        }

        val commonProject = findProject(":common")
        val fabricProject = findProject(":fabric")

        if (commonProject != null) {
            delete(commonProject.projectDir.resolve("src/main/resources/assets/mymod").path)
            delete(commonProject.projectDir.resolve("src/main/resources/data/mymod").path)
            delete(commonProject.projectDir.resolve("src/main/resources/mymod.mixins.json").path)
        }

        if (fabricProject != null)
            delete(fabricProject.projectDir.resolve("src/main/resources/mymod.classtweaker").path)
    }
    else {
        // Remove newly created directories
        logger.error("Failed to perform setup refactor, cleaning up")

        val group = project.properties["group"] as String
        val newPackage = group.replace(".", "/")

        for (module in arrayOf("common", "fabric", "forge", "neoforge")) {
            val project = findProject(":$module") ?: continue

            delete(project.projectDir.resolve("src/main/java/$newPackage/$modId").path)
            delete(project.projectDir.resolve("src/main/resources/META-INF/services/$group.$modId.platform.PlatformHelper").path)
        }

        val commonProject = findProject(":common")
        val fabricProject = findProject(":fabric")

        if (commonProject != null) {
            delete(commonProject.projectDir.resolve("src/main/resources/assets/$modId").path)
            delete(commonProject.projectDir.resolve("src/main/resources/data/$modId").path)
            delete(commonProject.projectDir.resolve("src/main/resources/$modId.mixins.json").path)
        }

        if (fabricProject != null) {
            delete(fabricProject.projectDir.resolve("src/main/resources/$modId.classtweaker").path)

            fabricProject.projectDir.resolve("src/main/resources/fabric.mod.json").takeIf(File::exists)?.let {
                it.writeText(it.readText().replaceFirst("$group.$modId", "io.github.myname.mymod"))
            }
        }
    }
})

/**
 * Refactor a given module's contents using the new modId
 */
private fun refactorModule(module: String): Boolean {
    val group = project.properties["group"] as String
    val newPackage = group.replace(".", "/")
    val project = findProject(":$module") ?: return true

    return withinDirectory(project, "src/main/java") { root ->
        refactorModuleSources(root, module, group, newPackage)
    } && withinDirectory(project, "src/main/resources") { root ->
        refactorModuleResources(root, module, group)
    }
}

/**
 * Refactor the <code>src/main/java/</code> sources for a given module
 */
private fun refactorModuleSources(root: File, module: String, group: String, newPackage: String): Boolean {
    return root.resolve("io/github/myname/mymod").takeIf(File::exists)?.let {
        return@let copy {
            from(it).filter { it ->
                var line = it.replace("io.github.myname.mymod", "$group.$modId")

                if (module == "common")
                    line = line.replaceFirst("String MODID = \"mymod\"", "String MODID = \"$modId\"")

                return@filter line
            }

            into(root.resolve("$newPackage/$modId"))
        }.didWork
    }?: true
}

/**
 * Refactor the <code>src/main/resources/</code> sources for a given module
 */
private fun refactorModuleResources(root: File, module: String, group: String): Boolean {
    var copied = true

    root.resolve("META-INF/services/io.github.myname.mymod.platform.PlatformHelper").takeIf(File::exists)?.let {
        copied = copy {
            from(it).filter { it ->
                it.replace("io.github.myname.mymod", "$group.$modId")
            }
            into(root.resolve("META-INF/services/"))
            rename("io.github.myname.mymod.platform.PlatformHelper", "$group.$modId.platform.PlatformHelper")
        }.didWork
    }

    if (module == "common") {
        if (copied) {
            root.resolve("assets/mymod").takeIf(File::exists)?.let {
                copied = copy {
                    from(it)
                    into(root.resolve("assets"))
                }.didWork || root.resolve("assets/$modId").mkdir()
            }
        }

        if (copied) {
            root.resolve("data/mymod").takeIf(File::exists)?.let {
                copied = copy {
                    from(it)
                    into(root.resolve("data"))
                }.didWork || root.resolve("data/$modId").mkdir()
            }
        }

        if (copied) {
            root.resolve("mymod.mixins.json").takeIf(File::exists)?.let {
                copied = copy {
                    from(it).filter { it ->
                        it.replace("io.github.myname.mymod", "$group.$modId")
                            .replaceFirst("mymod.refmap.json", "$modId.refmap.json")
                    }

                    into(root)
                    rename("mymod", modId)
                }.didWork
            }
        }
    }
    else if (module == "fabric") {
        if (copied) {
            root.resolve("mymod.classtweaker").takeIf(File::exists)?.let {
                copied = copy {
                    from(it)
                    into(root)
                    rename("mymod", modId)
                }.didWork
            }
        }

        if (copied) {
            root.resolve("fabric.mod.json").takeIf(File::exists)?.let {
                it.writeText(it.readText().replace("io.github.myname.mymod", "$group.$modId"))
            }
        }
    }

    return copied;
}

/**
 * Safety call to operate on a subdirectory of the root project directory
 */
private fun withinDirectory(project: Project, subPath: String, operation: (File) -> Boolean): Boolean {
    val file = project.projectDir.resolve(subPath)

    if (!file.exists())
        throw IllegalArgumentException("Provided invalid directory in while refactoring project, exiting for safety.")

    if (!checkFileIsInProject(file))
        throw IllegalArgumentException("Provided directory is not a child of the project, exiting for safety.")

    return operation.invoke(file)
}

/**
 * Safety check for files to ensure the file being worked on is contained within the project directory
 */
@Throws(FileException::class)
private fun checkFileIsInProject(file: File): Boolean {
    return file.canonicalFile.relativeToOrNull(project.projectDir.canonicalFile) != null
}

/**
 *
 * Perform pre-check to ensure the project is still in default form to prevent overwrites
 */
private fun isInitialUnmodifiedSetup(): Boolean {
    for (module in arrayOf("common", "fabric", "forge", "neoforge")) {
        val project = findProject(":$module")

        if (project != null && !project.projectDir.resolve("src/main/java/io/github/myname/mymod").exists())
            return false
    }

    val commonProject = findProject(":common")

    if (commonProject != null) {
        for (path in arrayOf(
            "src/main/resources/assets/mymod", "src/main/resources/assets/mymod",
            "src/main/resources/data/mymod", "src/main/resources/mymod.mixins.json")) {
            if (!commonProject.projectDir.resolve(path).exists())
                return false
        }
    }

    val fabricProject = findProject(":fabric")

    if (fabricProject != null) {
        if (!fabricProject.projectDir.resolve("src/main/resources/fabric.mod.json").exists())
            return false

        if (!fabricProject.projectDir.resolve("src/main/resources/mymod.classtweaker").exists())
            return false
    }

    return commonProject == null || commonProject.projectDir.resolve("src/main/java/io/github/myname/mymod/ModConstants.java").takeIf { it.exists() }?.let { it ->
        return@let it.readText().contains("MODID = \"mymod\"")
    }?: false
}