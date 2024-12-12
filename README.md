# Recipe App

Recipe App:
  - is a mobile application that allows users to explore, save, and manage recipes.
  - provides a detailed view of recipes, including ingredients, instructions, and nutritional information.
  - allows users to mark recipes as favorites, edit saved recipes, and store them in a local database for easy access.

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
