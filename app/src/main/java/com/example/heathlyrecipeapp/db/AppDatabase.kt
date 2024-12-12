package com.example.heathlyrecipeapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.heathlyrecipeapp.api.model.Recipe
import com.example.heathlyrecipeapp.utility.Converters

/**
 * Room database configuration for the app.
 *
 * The `AppDatabase` class provides the Room database instance and access to the `RecipeDao`.
 */
@Database(entities = [Recipe::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the DAO for recipe database operations.
     */
    abstract fun recipeDao(): RecipeDao

    companion object{

        /**
         * Singleton instance of the database.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null


        /**
         * Returns the singleton instance of the database, creating it if necessary.
         *
         * @param context The application context.
         * @return The database instance.
         */
        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Healthy Recipe App"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}