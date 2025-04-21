/**
 * 🚀 Summary
 * This configuration:
 * - Applies IntelliJ integration.
 * - Makes IntelliJ automatically download sources and Javadocs.
 * - Ensures that the full Gradle distribution is used via the wrapper.
 */

/**
 * This applies the idea plugin, which integrates your Gradle project with IntelliJ IDEA.
 *
 * It helps generate IntelliJ project files (.iml, .ipr, .iws) and allows configuring
 * how IntelliJ should behave with your project.
 *
 * This is useful in multi-module builds or when importing Gradle projects cleanly into IntelliJ.
 *
 * https://docs.gradle.org/current/userguide/idea_plugin.html
 */
plugins {
    idea
}

idea {
    module.isDownloadJavadoc = true // download Javadoc for your dependencies.
    module.isDownloadSources = true // download source code for your dependencies.
}

/**
 * This ensures when someone runs ./gradlew, Gradle will:
 * Download the full Gradle distribution (ALL) including:
 * - Core binaries
 * - Documentation
 * - Sources
 * Instead of just the minimal version (BIN) which skips docs and source code.
 * https://docs.gradle.org/current/userguide/gradle_wrapper.html#customizing_wrapper
 */
tasks {
    wrapper {
        distributionType = Wrapper.DistributionType.ALL
    }
}
