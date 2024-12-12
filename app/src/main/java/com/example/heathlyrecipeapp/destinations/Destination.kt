package com.example.heathlyrecipeapp.destinations

/**
 * Defines the available navigation destinations in the application.
 *
 * @property route The route string used for navigation.
 */
sealed class Destination(val route: String) {

    /**
     * Represents the home screen of the application.
     */
    object Home : Destination("home")

    /**
     * Represents the search screen where users can search for recipes.
     */
    object Search : Destination("search")

    /**
     * Represents the preferences screen for managing saved recipes or preferences.
     */
    object Preferences : Destination("preferences")

    /**
     * Represents the recipe detail screen that displays information about a specific recipe.
     *
     * @param compositeKey The unique key used to fetch the recipe details.
     */
    object  RecipeDetail : Destination("recipeDetail/{compositeKey}")
}