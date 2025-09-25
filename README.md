# Pokemon Compose App 🎮

A modern Android application built with Jetpack Compose that displays Pokemon information in a
beautiful Material Design carousel. The app follows Clean Architecture principles and MVVM pattern,
featuring efficient data loading with Paging 3.

## ✨ Features

- **Material Design Carousel**: Beautiful horizontal pager with smooth Pokemon cards
- **Auto-pagination**: Infinite scrolling with automatic data loading
- **Type-based Theming**: Dynamic colors based on Pokemon types
- **Error Handling**: Graceful error states and recovery mechanisms
- **Loading States**: Proper loading indicators for better UX
- **High-quality Images**: Official Pokemon artwork with fallback support

## 🏗️ Architecture

This project implements **Clean Architecture** with **MVVM** pattern:

### Architecture Layers

1. **Presentation Layer** (`presentation/`)
   - ViewModels managing UI state
   - Compose UI components and screens
   - Navigation with Voyager

2. **Domain Layer** (`domain/`)
   - Business models and entities
   - Repository interfaces

3. **Data Layer** (`data/`)
   - Repository implementations
   - Remote API services
   - Data mappers and DTOs

### SOLID Principles Applied

- ✅ **Single Responsibility**: Each class has one clear purpose
- ✅ **Open/Closed**: Repository interface allows different implementations
- ✅ **Liskov Substitution**: Repository implementations maintain contract
- ✅ **Interface Segregation**: Clean, focused interfaces
- ✅ **Dependency Inversion**: High-level modules depend on abstractions

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI framework
- **Material 3** - Google's latest design system

### Architecture & DI
- **MVVM** - Presentation architecture pattern
- **Koin** - Lightweight dependency injection framework
- **Voyager** - Type-safe navigation library

### Data & Networking
- **Retrofit** - HTTP client for API calls
- **Gson** - JSON serialization/deserialization
- **Paging 3** - Efficient data loading and pagination

### Image Loading

- **Coil** - Image loading library
- **Custom Image Downloader** - Local image handling

### Async & Reactive
- **Kotlin Coroutines** - Asynchronous programming
- **Flow** - Reactive stream processing

## 🎯 Project Structure

```
pokemon-compose/
├── src/main/java/com/example/pokemon_compose/
│   ├── data/
│   │   ├── mapper/           # DTO to Domain mapping
│   │   ├── remote/          # API services & paging
│   │   └── repository/      # Repository implementation
│   ├── di/
│   │   └── AppModule.kt     # Dependency injection setup
│   ├── domain/
│   │   └── model/           # Domain models
│   ├── presentation/
│   │   ├── pokemonlistscreen/
│   │   │   ├── components/  # Reusable UI components
│   │   │   └── PokemonListScreen.kt
│   │   └── viewmodel/       # ViewModels
│   ├── ui/theme/            # App theming
│   └── PokemonApplication.kt
└── build.gradle.kts         # Module dependencies
```

## 📱 App Flow Diagram

```mermaid
graph TD
    A[App Launch] --> B[MainActivity]
    B --> C[PokemonApplication]
    C --> D[Koin DI Setup]
    D --> E[PokemonListScreen]
    
    E --> F[PokemonListViewModel]
    F --> G[PokemonRepository]
   G --> H[PokemonApiService]
   H --> I[Pokemon API<br/>pokeapi.co]
   I --> J[AsyncPokePagingSource]
   J --> K[Load Pokemon Details]
   K --> L[Download Images]
   L --> M[ImageDownloadService]
   M --> N[Local Storage]
   N --> O[Update UI State]
   O --> P[PokemonCarousel]
   P --> Q[Pokemon Cards]
   Q --> R{User Scrolls}
   R -->|Near End| S[Load More Data]
   S --> J
   R -->|Continue Browsing| Q
    
    style A fill:#e1f5fe
   style I fill: #fff3e0
   style P fill: #f3e5f5
   style Q fill: #e8f5e8
```