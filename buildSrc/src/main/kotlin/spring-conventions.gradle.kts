/**
 * Applies several Gradle plugins to configure a Spring Boot + Kotlin project.
 */
plugins {
    /**
     * Applies a shared convention plugin (`java-conventions`) defined in `buildSrc`.
     * This typically includes settings like toolchains, compiler flags, encoding, etc.
     */
    id("java-conventions")

    /**
     * Applies the `kotlin-spring` compiler plugin.
     *
     * This makes Spring-compatible classes "open" automatically, even if you don't declare them as `open`.
     * It applies to classes annotated with:
     * - @Configuration
     * - @Component
     * - @Controller / @RestController
     * - @Service
     * - @Repository
     *
     * This is necessary because Kotlin classes are `final` by default, but Spring uses proxies that require `open` classes.
     *
     * Docs: https://kotlinlang.org/docs/compiler-plugins.html#spring-support
     */
    kotlin("plugin.spring")

    /**
     * Applies the `kotlin-jpa` compiler plugin.
     *
     * Automatically generates a no-arg constructor for classes annotated with:
     * - @Entity
     * - @MappedSuperclass
     * - @Embeddable
     *
     * This is required because JPA needs a default constructor (even if not used directly).
     *
     * Docs: https://kotlinlang.org/docs/no-arg-plugin.html#jpa-support
     */
    kotlin("plugin.jpa")

    /**
     * Applies the Spring Boot Gradle plugin.
     *
     * It:
     * - Enables building executable JARs/WARs
     * - Adds tasks like `bootRun` and `bootJar`
     * - Integrates Spring Boot Actuator, Devtools, and dependency management
     *
     * Docs: https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/
     */
    id("org.springframework.boot")

    /**
     * Applies Spring's Dependency Management plugin.
     *
     * It allows you to import BOMs (like `spring-boot-dependencies`) and align dependency versions.
     * Often used with `org.springframework.boot` plugin.
     *
     * Docs: https://github.com/spring-gradle-plugins/dependency-management-plugin
     */
    id("io.spring.dependency-management")
}

/**
 * Configures the `springBoot` plugin.
 *
 * `buildInfo {}` generates a `META-INF/build-info.properties` file
 * used by Spring Boot Actuator to display build metadata.
 *
 * Excluding the timestamp improves build reproducibility and caching.
 *
 * Docs: https://docs.spring.io/spring-boot/gradle-plugin/integrating-with-actuator.html
 */
springBoot {
    // Creates META-INF/build-info.properties for Spring Boot Actuator
    buildInfo {
        // Excludes time from build-info in favor of build cache
        // See https://docs.spring.io/spring-boot/gradle-plugin/integrating-with-actuator.html
        excludes.set(setOf("time"))
    }
}

dependencies {
    /**
     * Adds Spring Boot DevTools to the project.
     *
     * This is included in the `developmentOnly` configuration, meaning it is:
     * - Available at runtime in dev
     * - Excluded from production builds
     *
     * DevTools enables:
     * - Auto restart on code changes
     * - Live reload
     * - Better error pages
     *
     * Docs: https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools
     */
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

/**
 * Custom dependency resolution strategy for the `detekt` configuration.
 *
 * This ensures that **all Kotlin dependencies used by Detekt** match
 * the Kotlin version Detekt itself supports.
 *
 * Why? Because Detekt uses the Kotlin compiler under the hood. If your project uses a newer Kotlin version than Detekt expects, it can crash.
 *
 * This block forces Detekt to use the version of Kotlin that matches its internal compiler support.
 */
dependencyManagement {
    configurations.matching { it.name == "detekt" }.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                @Suppress("UnstableApiUsage")
                // Replace requested Kotlin version with the version supported by Detekt
                useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
            }
        }
    }
}
