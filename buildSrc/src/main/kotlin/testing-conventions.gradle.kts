/**
 * Imports constants for test logging configuration.
 *
 * - `FULL` shows the full stack trace and exception messages for failed tests.
 * - `FAILED` ensures only failed test events are logged.
 */
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED

plugins {
    /**
     * Applies the shared Java conventions plugin from `buildSrc`.
     *
     * This likely configures:
     * - Java toolchain
     * - Compiler options
     * - UTF-8 encoding
     * - Common tasks (like `jar` manifest config)
     */
    id("java-conventions")
}

/**
 * Configures the built-in `test` task provided by the `java` plugin.
 *
 * Sets up JUnit 5 support and improves test output logging.
 */
tasks.test {
    /**
     * Enables support for JUnit 5 via the JUnit Platform.
     * This is required when using the `junit-jupiter` API.
     *
     * Without this, Gradle would default to the older JUnit 4 test engine.
     */
    useJUnitPlatform()

    /**
     * Configures how test logs are displayed in the console.
     */
    testLogging {
        /**
         * Only logs events for failed tests.
         * Options include: PASSED, SKIPPED, FAILED, STANDARD_OUT, etc.
         */
        events = setOf(FAILED)

        /**
         * Displays the **full exception** message and stack trace for failed tests.
         * This helps a lot with debugging test failures during development or in CI.
         */
        exceptionFormat = FULL
    }
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
