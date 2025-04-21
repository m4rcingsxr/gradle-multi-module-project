/**
 * Enables a custom version catalog named libs
 * Loads it from a relative path: ../gradle/libs.versions.toml
 * Makes all versions and libraries in that file accessible to the build via libs.<whatever>
 */
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

