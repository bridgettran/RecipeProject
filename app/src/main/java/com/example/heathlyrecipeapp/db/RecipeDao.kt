package com.example.heathlyrecipeapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.heathlyrecipeapp.api.model.Recipe

/**
 * Data Access Object (DAO) for Recipe database operations.
 *
 * The `RecipeDao` interface provides methods to insert, query, and update recipes
 * stored in the Room database. It defines SQL queries and operations using Room annotations.
 */
@Dao
interface RecipeDao {

    /**
     * Inserts a list of recipes into the database.
     * If a recipe with the same primary key exists, it will be replaced.
     *
     * @param recipes The list of recipes to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRecipes(recipes: List<Recipe>)

    /**
     * Retrieves a recipe from the database using its composite key.
     *
     * @param compositeKey The unique key of the recipe to retrieve.
     * @return The matching recipe, or null if not found.
     */
    @Query("SELECT * FROM recipes WHERE compositeKey = :compositeKey")
    fun getRecipeByCompositeKey(compositeKey: String): Recipe?

    /**
     * Updates an existing recipe in the database.
     * Only recipes with a matching primary key will be updated.
     *
     * @param recipe The recipe to update.
     */
    // update recipes where composite key matches
    @Update
    fun updateRecipe(recipe: Recipe)

}