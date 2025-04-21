import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

/**
 * It sets up the module as a Gradle Kotlin DSL plugin project, meaning:
 * It can contain reusable build logic — like:
 * - Custom Gradle plugins
 * - Common tasks
 * - Dependency configurations
 */
plugins {
    `kotlin-dsl`
}

/**
 * This block tells Gradle which repositories to search when resolving:
 * - Plugin dependencies (plugins { ... })
 *  - This adds the Gradle Plugin Portal to your project.
 *    Fetching Gradle plugins declared in the plugins { ... } block
 *    Examples: id("org.jetbrains.kotlin.jvm"), id("com.github.ben-manes.versions"), etc.
 * - Regular dependencies (implementation(...), etc.)
 *  - This adds Maven Central, the most popular public Java/Kotlin artifact repository.
 */
repositories {
    gradlePluginPortal()
    mavenCentral()
}

/**
 * Configures the Java toolchain (i.e., which JDK version Kotlin will compile against).
 * libs.versions.jdk.get() retrieves the JDK version from libs.versions.toml.
 */
kotlin {
    jvmToolchain {
        languageVersion.set(
            JavaLanguageVersion.of(libs.versions.jdk.get())
        )
    }

    /**
     * This block configures Kotlin compiler options, like flags,
     * target JVM version, language version, etc.
     */
    compilerOptions {
        /**
         * - Compiler will treat seriously null related annotation
         * - Enables strict nullability handling for Java annotations (@Nullable, @NotNull) based on JSR-305.
         * - This improves null-safety when calling Java code from Kotlin.
         */
        freeCompilerArgs.add("-Xjsr305=strict")

        // Treats all compiler warnings as errors, enforcing stricter code quality.
        allWarningsAsErrors = true

        /**
         * - Ensures the compiled Kotlin bytecode matches your JDK version
         * - Sets the JVM bytecode target version (e.g., JVM_21).
         */
        jvmTarget.set(JvmTarget.valueOf("JVM_${libs.versions.jdk.get()}"))

        /**
         * Sets the Kotlin language version (e.g., KOTLIN_1_9).
         * Takes the version from libs.versions.kotlin (e.g., "1.9.0"), trims the patch,
         * replaces dot with underscore, and maps it to enum KotlinVersion.KOTLIN_1_9.
         */
        languageVersion.set(
            KotlinVersion.valueOf(
                "KOTLIN_${
                    libs.versions.kotlin.get().substringBeforeLast(".").replace(".", "_")
                }"
            )
        )

        /**
         * Sets the Kotlin API version — the set of language features you're allowed to use,
         * even if you're compiling with a newer compiler.
         * This allows gradual upgrades: compile with a new Kotlin compiler, but limit yourself to an older API.
         */
        apiVersion.set(
            KotlinVersion.valueOf(
                "KOTLIN_${
                    libs.versions.kotlin.get().substringBeforeLast(".").replace(".", "_")
                }"
            )
        )
    }
}

/**
 * This dependencies block is specific to the `buildSrc` module and is used to include
 * the required Gradle plugins **as regular dependencies**.
 *
 * Why? Because precompiled convention plugins in `buildSrc` need access to the actual
 * plugin classes (e.g., Spring Boot, Kotlin, Dokka), but not through `plugins {}`.
 *
 * Gradle will not auto-resolve plugin IDs in Kotlin DSL scripts unless the corresponding
 * plugin artifact is explicitly declared in `dependencies`.
 *
 * By declaring them here, you ensure:
 * - All convention plugins (e.g., `kotlin-conventions.gradle.kts`) in `buildSrc` can
 *   apply these plugins without issues.
 * - Plugin versions stay consistent via `libs.versions.toml` (version catalog).
 */
dependencies {
    // buildSrc in combination with this plugin ensures that the version set here
    // will be set to the same for all other Kotlin dependencies / plugins in the project.
    implementation(libs.kotlin.gradlePlugin)

    // https://kotlinlang.org/docs/all-open-plugin.html
    // contains also https://kotlinlang.org/docs/all-open-plugin.html#spring-support
    // The all-open compiler plugin adapts Kotlin to the requirements of those frameworks and makes classes annotated
    // with a specific annotation and their members open without the explicit open keyword.
    implementation(libs.kotlin.allopenPlugin)

    // https://kotlinlang.org/docs/no-arg-plugin.html
    // contains also https://kotlinlang.org/docs/no-arg-plugin.html#jpa-support
    // The no-arg compiler plugin generates an additional zero-argument constructor for classes
    // with a specific annotation.
    implementation(libs.kotlin.noargPlugin)

    // https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/
    // The Spring Boot Gradle Plugin provides Spring Boot support in Gradle.
    // It allows you to package executable jar or war archives, run Spring Boot applications,
    // and use the dependency management provided by spring-boot-dependencies
    implementation(libs.springBoot.gradlePlugin)

    // https://github.com/Kotlin/dokka
    // Dokka is a documentation engine for Kotlin like JavaDoc for Java
    implementation(libs.dokka.gradlePlugin)

    implementation(libs.spring.dependencyManagementPlugin)

    // https://detekt.dev/docs/gettingstarted/gradle/
    // A static code analyzer for Kotlin
    implementation(libs.detekt.gradlePlugin)
}
