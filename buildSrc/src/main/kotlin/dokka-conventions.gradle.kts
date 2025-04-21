/**
 * Applies the Dokka plugin to the project.
 *
 * The Dokka plugin is used to generate documentation for Kotlin projects.
 * It supports multiple output formats like HTML, GFM (Markdown), and Javadoc-style HTML.
 * This plugin enables tasks such as `dokkaHtml`, `dokkaJavadoc`, etc.
 *
 * Plugin ID reference: https://plugins.gradle.org/plugin/org.jetbrains.dokka
 */
plugins {
    id("org.jetbrains.dokka")
}

/**
 * Registers a Dokka plugin dependency — specifically, the "kotlin-as-java" plugin.
 *
 * Dokka supports plugin extensions that can modify or enhance how documentation is generated.
 * The `kotlin-as-java-plugin` transforms Kotlin declarations into a style that resembles Java-style APIs.
 * This is useful when you're documenting Kotlin code but want the output to look familiar to Java users,
 * e.g., for published libraries with Java-facing consumers.
 *
 * `dokkaHtmlPlugin` is a special configuration used by the Dokka plugin to load extensions at documentation time.
 */
dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin")
}

/**
 * Configures the `dokkaJavadoc` task.
 *
 * Dokka can generate Javadoc-style HTML documentation via the `dokkaJavadoc` task.
 * This block modifies the output location of the generated docs to:
 *     build/javadoc/
 *
 * This helps keep generated docs organized and predictable across modules.
 *
 * You can run this task via:
 *     ./gradlew dokkaJavadoc
 *
 * Note:
 * - You can similarly configure other tasks like `dokkaHtml` or `dokkaGfm` if needed.
 * - Dokka uses Kotlin's reflection and compiler API under the hood to extract documentation.
 */
tasks.dokkaJavadoc.configure {
    outputDirectory.set(layout.buildDirectory.dir("javadoc"))
}
