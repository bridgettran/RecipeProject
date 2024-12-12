package com.example.heathlyrecipeapp.api

import com.example.heathlyrecipeapp.api.model.RecipeData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines API endpoints for fetching recipes.
 *
 * The `RecipesService` interface contains methods for making network requests
 * to fetch recipes using Retrofit.
 */
interface RecipesService{

    /**
     * Fetches random recipes from the API.
     *
     * @param appId The Edamam app ID.
     * @param appKey The Edamam app key.
     * @param type The type of API request (default is "public").
     * @param query The query string for the recipe search.
     *
     * @return A `Call` object for the API response.
     */
    @GET("api/recipes/v2")
    fun getRandomRecipes(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("type") type: String = "public",
        @Query("q") query: String = "recipe" // e.g., "popular"
    ): Call<RecipeData>

    /**
     * Searches for recipes by name.
     *
     * @param appId The Edamam app ID.
     * @param appKey The Edamam app key.
     * @param type The type of API request (default is "public").
     * @param query The query string for the recipe search.
     *
     * @return A `Call` object for the API response.
     */
   @GET("api/recipes/v2")
    fun searchRecipeByName(
       @Query("app_id") appId: String,
       @Query("app_key") appKey: String,
       @Query("type") type: String = "public",
       @Query("q") query: String
    ): Call<RecipeData>

    /**
     * Fetches recipes filtered by a health label.
     *
     * @param appId The Edamam app ID.
     * @param appKey The Edamam app key.
     * @param type The type of API request (default is "public").
     * @param healthLabel The health label for filtering recipes (e.g., "vegan").
     *
     * @return A `Call` object for the API response.
     */
    @GET("api/recipes/v2")
    fun getRecipesByHealthLabel(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("type") type: String = "public",
        @Query("health") healthLabel: String
    ): Call<RecipeData>

}