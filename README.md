# PaginationDemo


## Tech Stack (main Branch)
- Kotlin 2.0
- Jetpack Compose
- Hilt
- Room
- Retrofit with Kotlinx Serialization
- Paging 3
- Clean Architecture
- KSP
- TOML (Version Catalogs)

## Tech Stack (second Branch)
- Hilt --> Koin
- Room --> SqlDelight
- Retrofit --> Ktor

## Project Structure

PaginationDemo/
- app/ # Android application module
- data/ # Data layer: repositories, data sources, models
- domain/ # Domain layer: use cases, business logic
- ui/ # UI module with Compose screens and previews
- build.gradle.kts
- settings.gradle.kts

## Features

- Displays a list of items from a api using an infinite scroll
- Caches data in a local Room database for offline access
- Uses Kotlin Flows for reactive data streams
- Modern Android development stack using Jetpack Compose
- Dependency Injection with Hilt (or Koin)
- Pagination logic with UI loading indicators
- Modularized codebase for maintainability and testability
- Swipe to refresh
- Error cases

## What missing

- Add code quality : KtLint, Detekt
- Add Ui tests 
