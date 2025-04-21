# Microservices with Gradle Kotlin DSL

This repository is a **multi-module microservices setup** built with **Spring Boot**, using **Gradle Kotlin DSL** for clean, reusable, and centralized build configuration. Each microservice is isolated in its own module, and shared build logic is managed via a dedicated `buildSrc` module.

---

##  Project Structure

```
microservices/
├── app-accounts/              # Microservice handling account-related logic
├── app-dashboard/             # Microservice rendering a dashboard using j2html
├── buildSrc/                  # Shared Gradle logic (convention plugins)
│   └── src/main/kotlin/
│       ├── dokka-conventions.gradle.kts
│       ├── java-conventions.gradle.kts
│       ├── kotlin-conventions.gradle.kts
│       ├── spring-conventions.gradle.kts
│       └── testing-conventions.gradle.kts
├── .gitattributes             # Git settings for line endings, binary handling, etc.
├── .gitignore                 # Files and directories excluded from version control
├── build.gradle.kts           # Root Gradle build script
├── settings.gradle.kts        # Includes modules in the build
├── gradle/ + wrapper files    # Gradle wrapper setup
```

---

##  Key Concepts

###  Multi-Module Gradle Build

This project uses Gradle's [multi-project build](https://docs.gradle.org/current/userguide/intro_multi_project_builds.html) to manage microservices. It allows:

- Centralized dependency/version management
- Reusable plugin logic via `buildSrc`
- Cleaner separation of concerns between services

---

###  buildSrc (Convention Plugins)

The `buildSrc` directory contains reusable Gradle configuration logic using Kotlin. These convention plugins are applied automatically in each subproject.

#### Included Plugins

| File                                | Purpose                                |
|-------------------------------------|----------------------------------------|
| `kotlin-conventions.gradle.kts`     | Kotlin compiler settings and warnings |
| `java-conventions.gradle.kts`       | Java versioning and compiler flags    |
| `spring-conventions.gradle.kts`     | Spring Boot plugin config and defaults|
| `testing-conventions.gradle.kts`    | JUnit/Test libraries and configs      |
| `dokka-conventions.gradle.kts`      | Dokka documentation generation        |

---

### Run a Microservice

```bash
./gradlew :app-accounts:bootRun
./gradlew :app-dashboard:bootRun
```

> Each microservice runs on its own port.

### Build Everything

```bash
./gradlew build
```

---

##  Microservices Overview

| Module         | Description                                     | Port  |
|----------------|-------------------------------------------------|--------|
| `app-accounts` | Provides account data via REST API              | 8081   |
| `app-dashboard`| Renders an HTML dashboard using j2html          | 8082   |

---

## Tech Stack

- Kotlin
- Spring Boot
- Gradle Kotlin DSL
- j2html
- Detekt
- JUnit 5

---

##  Why This Setup?

- ✅ Modular: Each service lives in its own project
- ✅ DRY: Shared logic lives in `buildSrc` with clean Gradle Kotlin DSL
- ✅ Scalable: Easy to add more services or convention plugins

---
