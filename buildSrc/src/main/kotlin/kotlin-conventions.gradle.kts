import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    /**
     * Applies a custom convention plugin named `java-conventions`.
     * This is a reusable configuration defined in `buildSrc` that sets up Java compiler options, toolchains, etc.
     */
    id("java-conventions")

    /**
     * Applies the Kotlin JVM plugin to add support for compiling Kotlin code to the JVM.
     */
    kotlin("jvm")

    /**
     * Applies the Detekt plugin for static code analysis of Kotlin code.
     * It helps enforce code style and detect common mistakes.
     */
    id("io.gitlab.arturbosch.detekt")

    /**
     * This plugin is disabled due to an open issue in Detekt:
     * https://github.com/detekt/detekt/issues/6958
     * It's commented out until that issue is resolved.
     */
    // id("io.github.detekt.gradle.compiler-plugin")
}

/**
 * Loads the version catalog defined in `libs.versions.toml` under the alias `libs`.
 * This gives access to centrally-managed dependency and version references.
 */
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

kotlin {
    /**
     * Configures the Java toolchain for Kotlin compilation.
     * Ensures a consistent JDK version (e.g., JDK 21) across all machines.
     */
    jvmToolchain {
        languageVersion.set(
            JavaLanguageVersion.of(libs.findVersion("jdk").get().toString())
        )
    }


    /**
     * Configures Kotlin compiler options for all source sets.
     */
    compilerOptions {
        /**
         * Enables strict nullability checking for Java annotations like @Nullable/@NotNull.
         */
        @Suppress("SpellCheckingInspection")
        freeCompilerArgs.add("-Xjsr305=strict")

        /**
         * Fails the build on any compiler warning to enforce high code quality.
         */
        allWarningsAsErrors = true

        /**
         * Sets the JVM bytecode target to match the configured JDK version.
         */
        jvmTarget.set(JvmTarget.valueOf("JVM_${libs.findVersion("jdk").get()}"))

        /**
         * Sets the Kotlin language version based on the version defined in the version catalog.
         * Converts a version like "2.1.20" to "KOTLIN_2_1".
         */
        languageVersion.set(
            KotlinVersion.valueOf(
                "KOTLIN_${
                    libs.findVersion("kotlin").get().toString().substringBeforeLast(".").replace(".", "_")
                }"
            )
        )

        /**
         * Sets the Kotlin API version (the set of available language features).
         * This lets you use a newer compiler while keeping compatibility with an older language version.
         */
        apiVersion.set(
            KotlinVersion.valueOf(
                "KOTLIN_${
                    libs.findVersion("kotlin").get().toString().substringBeforeLast(".").replace(".", "_")
                }"
            )
        )
    }
}

/**
 * Configures the Detekt static code analysis tool.
 * This helps enforce coding standards and detect anti-patterns in Kotlin codebases.
 */
detekt {
    /**
     * Enables parallel processing to speed up analysis in large projects.
     */
    parallel = true

    /**
     * Uses a custom Detekt configuration file defined in the root directory of the project.
     * This can override or add to the default Detekt rules.
     */
    config.setFrom("$rootDir/detekt.yml")

    /**
     * Ensures that custom configuration is layered on top of Detekt's default rules.
     * Allows you to extend rather than replace the defaults.
     */
    buildUponDefaultConfig = true

    /**
     * Enables automatic fixing of formatting issues when NOT running on CI.
     * Prevents CI from modifying code automatically during builds.
     */
    if (System.getenv("CI").isNullOrEmpty()) {
        autoCorrect = true
    }
}

/**
 * Configures all Detekt tasks to ignore generated code directories,
 * which are often irrelevant for static analysis.
 */
tasks.withType<Detekt>().configureEach {
    exclude("**/generated/**")
}

/**
 * Makes the `check` task (which runs all project verification tasks) depend on `detektMain`.
 * This ensures Detekt runs automatically with `./gradlew check`.
 */
tasks.check {
    dependsOn(tasks.detektMain)
}

/**
 * Explicitly removes dependency on `detekt` task (the umbrella task),
 * leaving only `detektMain` and other specific tasks to run.
 * Prevents unnecessary duplicate executions.
 */
tasks.check.configure {
    this.setDependsOn(
        this.dependsOn.filterNot {
            it is TaskProvider<*> && it == tasks.detekt
        }
    )
}

dependencies {
    /**
     * Declares a version constraint to ensure the correct version of the Kotlin standard library is used.
     */
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }

    /**
     * Aligns versions of all Kotlin components using the Kotlin BOM (Bill of Materials).
     * Ensures consistent Kotlin versions across stdlib, reflection, coroutines, etc.
     */
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    /**
     * Uses the standard Kotlin JDK 8-compatible runtime.
     */
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    /**
     * Adds the Detekt formatting plugin which auto-formats code (like ktlint).
     * Plugin is resolved via the version catalog.
     */
    add("detektPlugins", libs.findLibrary("detekt-formatting").get())
}
