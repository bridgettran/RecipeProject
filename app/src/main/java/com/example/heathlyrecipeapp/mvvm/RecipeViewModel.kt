package com.example.heathlyrecipeapp.mvvm

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.heathlyrecipeapp.api.Api
import com.example.heathlyrecipeapp.api.model.Recipe
import com.example.heathlyrecipeapp.api.model.RecipeData
import com.example.heathlyrecipeapp.db.AppDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel for managing recipe data and API interactions.
 *
 * This object handles fetching recipes from a remote API, updating local data, and managing the UI state for recipes.
 */
object RecipeViewModel: ViewModel() {

    val api_id: String = "2d6cface"
    val api_key: String = "1683529fc2b67960c2830e9db4eb2b00"

    val recipes = mutableStateOf<List<Recipe>>(emptyList())
    val searchTerm = mutableStateOf("")

    // icon state
    var recipeIconState = mutableStateOf<Map<String, Boolean>>(emptyMap())

    val recipesResponse: MutableState<List<Recipe>>
        @Composable get() = remember {
            recipes
        }

    /**
     * Updates the favorite icon state for a given recipe.
     *
     * @param compositeKey The unique key identifying the recipe.
     * @param database The local database instance for updating the recipe's state.
     */
    fun updateRecipeIconState(compositeKey: String, database: AppDatabase) {
        GlobalScope.launch {
            database.recipeDao().getRecipeByCompositeKey(compositeKey).let { recipe ->
                recipe?.isFavorite = !recipe?.isFavorite!!
                database.recipeDao().updateRecipe(recipe)

                recipeIconState.value = recipeIconState.value.toMutableMap().apply {
                    this[compositeKey] = recipe.isFavorite!! // can be non-value

                }
            }
        }
    }

    /**
     * Fetches the list of recipes matching the provided name.
     *
     * @param recipeName The name of the recipe to search for.
     * @param database The local database instance for storing fetched recipes.
     */
        @OptIn(DelicateCoroutinesApi::class)
        fun searchRecipes(recipeName: String, selectedFilter: String, database: AppDatabase) {
            // api call
            val service =
                Api.retrofitService.searchRecipeByName(api_id, api_key, "public", recipeName)
            service.enqueue(object : Callback<RecipeData> {
                override fun onResponse(call: Call<RecipeData>, response: Response<RecipeData>) {
                    if (response.isSuccessful) {
                        Log.i("SearchData", "testing testing")
                        recipes.value = response.body()?.hits?.map { it.recipe } ?: emptyList()
                        Log.i("RecipeFound", recipes.toString())
                        GlobalScope.launch {
                            database.recipeDao().insertAllRecipes(recipes = recipes.value)
                        }
                    }
                }

                override fun onFailure(call: Call<RecipeData>, t: Throwable) {
                    Log.d("error", "${t.message}")
                }

            })

        }

        fun saveSearchTerm(term: String) {
            searchTerm.value = term
        }
}

