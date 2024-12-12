package com.example.heathlyrecipeapp.api

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.heathlyrecipeapp.api.model.Recipe
import com.example.heathlyrecipeapp.api.model.RecipeData
import com.example.heathlyrecipeapp.db.AppDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

/**
 * Manages recipes fetched from the API and handles database operations.
 *
 * The `RecipesManager` class retrieves recipes using the API, updates the UI state,
 * and saves the recipes to the database. It supports fetching random recipes and
 * storing them in a Room database.
 *
 * @property database The Room database instance for saving recipes.
 */
class RecipesManager(database: AppDatabase) {
    /**
     * State holder for the list of recipes fetched from the API.
     */
    private var _recipesResponse = mutableStateOf<List<Recipe>>(emptyList())

    /**
     * Edamam API credentials.
     */
    val api_id: String = "2d6cface"
    val api_key: String = "1683529fc2b67960c2830e9db4eb2b00"

    /**
     * Public access to the recipes state. Uses Jetpack Compose's `remember`.
     */
    val recipesResponse: MutableState<List<Recipe>>
        @Composable get() = remember {
            _recipesResponse
        }

    /**
     * Initializes the manager by fetching recipes from the API.
     */
    init {
        getRecipes(database)
    }

    /**
     * Fetches recipes from the Edamam API and updates the state.
     *
     * The function makes an asynchronous API call to retrieve random recipes.
     * On success, it updates the UI state and saves the recipes to the database.
     *
     * @param database The Room database instance for saving recipes.
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun getRecipes(database: AppDatabase) {
        val service = Api.retrofitService.getRandomRecipes(api_id, api_key, "public", "healthy")

        service.enqueue(object : Callback<RecipeData> {
            override fun onResponse(
                call: Call<RecipeData>,
                response: retrofit2.Response<RecipeData>


            ) {
                if (response.isSuccessful) { //200
                    Log.i("Data", "Data Loaded")
                    _recipesResponse.value = response.body()?.hits?.map { it.recipe } ?: emptyList()
                    Log.i("DataStream", _recipesResponse.toString())

                    GlobalScope.launch {
                        saveDataToDatabase(database, _recipesResponse.value)
                    }
                } else {
                    Log.e("Data Error", "Failed to load recipes: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<RecipeData>, t: Throwable) {
                Log.e("Data Error", "Network failure: ${t.message}")
            }
        }
        )
    }

    /**
     * Saves the fetched recipes to the database.
     *
     * @param database The Room database instance.
     * @param recipes The list of recipes to save.
     */
    private suspend fun saveDataToDatabase(database: AppDatabase, recipes: List<Recipe>) {
        Log.i("RDB", "Save Data function called")
        database.recipeDao().insertAllRecipes(recipes)
    }
}