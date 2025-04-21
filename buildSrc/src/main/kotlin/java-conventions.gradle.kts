/**
 * Applies the built-in Java plugin to enable Java compilation,
 * JAR packaging, and common Java-related tasks like `compileJava`, `jar`, and `test`.
 */
plugins {
    java
}

/**
 * Retrieves the `libs` version catalog extension to access centralized versions
 * defined in `libs.versions.toml`. This allows consistent versioning across projects.
 */
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Configures the Java plugin to use the Java Toolchain feature.
 *
 * Toolchains allow specifying a target JDK version independent of the local system JDK.
 * Here, the JDK version is pulled from the version catalog (`libs.versions.jdk`),
 * ensuring consistency across all modules and environments.
 *
 * Reference: https://docs.gradle.org/current/userguide/toolchains.html
 */
java {
    // Auto JDK setup
    toolchain {
        languageVersion.set(
            JavaLanguageVersion.of(libs.findVersion("jdk").get().toString())
        )
    }
}

/**
 * Configures the `compileJava` task with strict compiler flags.
 *
 * - Enables all recommended compiler warnings with `-Xlint:all`.
 * - Fails the build if any warning is detected using `-Werror`.
 * - Sets UTF-8 as the default file encoding for consistent behavior across systems.
 *
 * Reference for `javac` options: https://docs.oracle.com/en/java/javase/12/tools/javac.html
 */
tasks.compileJava {
    // See: https://docs.oracle.com/en/java/javase/12/tools/javac.html
    @Suppress("SpellCheckingInspection")
    options.compilerArgs.addAll(
        listOf(
            "-Xlint:all", // Enables all recommended warnings.
            "-Werror", // Terminates compilation when warnings occur.
        )
    )
    options.encoding = "UTF-8"
}

/**
 * Configures the `jar` task's manifest metadata.
 *
 * This embeds useful metadata into the generated JAR file, such as:
 * - `Implementation-Title`: the name of the project
 * - `Implementation-Version`: the current project version
 *
 * This metadata can be read later by tools or during runtime for diagnostics or logging.
 *
 * Reference: https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.Jar.html
 */
tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
            )
        )
    }
}

/**
 * Declares `mavenCentral()` as the repository for resolving dependencies.
 *
 * Maven Central is the most widely used public repository for open-source libraries.
 * It's used here to resolve any external dependencies your Java code may declare.
 */
repositories {
    mavenCentral()
}
