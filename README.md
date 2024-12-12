# Recipe App

Recipe App:
  - is a mobile application that allows users to explore, save, and manage recipes.
  - provides a detailed view of recipes, including ingredients, instructions, and nutritional information.
  - allows users to mark recipes as favorites, edit saved recipes, and store them in a local database for easy access.

## Features

### API Integration
 - Fetches recipes from the Edamam API based on user queries.
 - Provides three API endpoints:
 - Random Recipes:
  Fetches trending or popular recipes.
  Search by Name: Allows users to search for recipes by name or keyword.
  Filter by Health Label: Enables users to find recipes based on health filters like "vegan," "gluten-free," or "low-carb."

### Data Persistence
  Users can manage stored recipes through:
  - Adding ingredients.
  - Deleting ingredients.
  - Updating ingredient details.

### User Authentication
  Firebase Authentication provides:
  - Account registration.
  - Secure password reset functionality.
  - Account deletion for full control.

## Project Folder Structure
```text
com.example.healthyrecipeapp/
├── api/
│   ├── model/
│   │   ├── Api.kt
│   │   ├── RecipesManager.kt
│   │   └── RecipesService.kt
│   └── Api-related logic for Edamam integration.
├── db/
│   ├── AppDatabase.kt
│   └── RecipeDao.kt
├── destinations/
│   └── Destination.kt
├── mvvm/
│   └── RecipeViewModel.kt
├── navigation/
│   └── Navigation.kt
├── screens/
│   ├── AuthScreen.kt
│   ├── HomeScreen.kt
│   ├── RecipeDetailScreen.kt
│   ├── SavedScreen.kt
│   ├── SearchScreen.kt
│   └── SignInScreen.kt
├── ui/
│   └── UI components and styling logic.
├── utility/
│   ├── MainActivity.kt
│   └── SignInActivity.kt
```

## Key Components

### API Folder
 - Api.kt - Initializes Retrofit for API calls.
 - RecipesManager.kt - Manages API calls and data retrieval.
 - RecipesService.kt - Defines endpoints for fetching recipes using the Edamam API.

### Database Folder
 - AppDatabase.kt - Configures Room Database for storing recipe data.
 - RecipeDao.kt - Provides DAO methods for CRUD operations on recipes.

### Screens
 - HomeScreen: Displays trending/popular recipes.
 - RecipeDetailScreen: Shows detailed recipe information.
 - SearchScreen: Allows users to search recipes.
 - SavedScreen: Displays saved recipes.
 - AuthScreen/SignInScreen: Handles user authentication

## Development Tools
 - Language: Kotlin
 - Architecture: MVVM
 - UI Framework: Jetpack Compose
 - Backend Services: Firebase Authentication, Edamam API

## Future Improvemets 
 - Add offline support for saved recipes.
 - Introduce advanced filters (e.g., calorie range, cuisine type).
 - Enhance accessibility and responsiveness for tablets.
