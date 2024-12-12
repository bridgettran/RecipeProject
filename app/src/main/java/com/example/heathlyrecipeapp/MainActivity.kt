package com.example.heathlyrecipeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.heathlyrecipeapp.api.RecipesManager
import com.example.heathlyrecipeapp.api.model.Recipe
import com.example.heathlyrecipeapp.db.AppDatabase
import com.example.heathlyrecipeapp.destinations.Destination
import com.example.heathlyrecipeapp.mvvm.RecipeViewModel
import com.example.heathlyrecipeapp.navigation.BottomNav
import com.example.heathlyrecipeapp.screens.SearchScreen
import com.example.heathlyrecipeapp.screens.HomeScreen
import com.example.heathlyrecipeapp.screens.PreferencesScreen
import com.example.heathlyrecipeapp.screens.RecipeDetailScreen
import com.example.heathlyrecipeapp.ui.theme.HeathlyRecipeAppTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Main activity of the Recipe App.
 *
 * This activity initializes the app's main theme, navigation structure, database, and ViewModel.
 * It serves as the entry point to the application and sets up the navigation host and top-level UI.
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is created.
     *
     * This method initializes:
     * - The Jetpack Compose content.
     * - The app's database and ViewModel.
     * - The main navigation structure.
     *
     * @param savedInstanceState If non-null, this activity is being re-initialized after
     * previously being shut down.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeathlyRecipeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    //database
                    val db = AppDatabase.getInstance(applicationContext)

                    // fetch movie data from api
                    val recipesManager:RecipesManager = RecipesManager(db)

                    // initialize the viewmodel
                    val viewModel: RecipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

                    // firestore database
                    val fs_db = Firebase.firestore

                    App(navController = navController, modifier = Modifier.padding(innerPadding), recipesManager, db, viewModel, fs_db)
                }
            }
        }
    }
}

/**
 * Sets up the main UI for the Recipe App.
 *
 * This composable function defines the top-level Scaffold, including the top app bar,
 * navigation host, and bottom navigation bar. It manages navigation between different
 * screens and provides dependencies to the screens.
 *
 * @param navController The NavController for managing navigation between screens.
 * @param modifier The Modifier to be applied to the composable.
 * @param recipesManager The RecipesManager instance for API interaction and data handling.
 * @param db The local AppDatabase instance for managing persistent data.
 * @param viewModel The RecipeViewModel instance for managing app state and business logic.
 * @param fs_db The Firebase Firestore instance for syncing and storing data in the cloud.
 *
 * @return This composable does not return anything but sets up the UI and navigation.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    recipesManager: RecipesManager,
    db: AppDatabase,
    viewModel: RecipeViewModel,
    fs_db: FirebaseFirestore
){
    var recipe by remember {
        mutableStateOf<Recipe?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Healthy Shopping & Recipe Planner") }
            )
        },
        content = { innerPadding ->
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
            )
            NavHost(navController = navController as NavHostController, startDestination = Destination.Home.route){
                composable(Destination.Home.route){
                    HomeScreen(modifier = modifier, recipesManager, navController)
                }
                composable(Destination.Search.route){
                    SearchScreen(modifier = Modifier.padding(innerPadding), viewModel, db, navController)
                }
                /*composable(Destination.Inventory.route){
                    InventoryScreen()
                }*/
                composable(Destination.Preferences.route){
                    PreferencesScreen(modifier = Modifier.padding(innerPadding), navController)
                }
                composable(Destination.RecipeDetail.route){ navBackStackEntry ->
                    val composite_key:String? = navBackStackEntry.arguments?.getString("compositeKey")
                    GlobalScope.launch{
                        if (composite_key != null){
                            recipe = db.recipeDao().getRecipeByCompositeKey(composite_key)
                        }
                    }

                    /*recipe?.let { RecipeDetailScreen(it, modifier = Modifier.padding(),viewModel, db, fs_db)}*/
                    recipe?.let {
                        RecipeDetailScreen(
                            recipe = it,
                            modifier = Modifier.padding(),
                            viewModel = viewModel,
                            database = db,
                            fs_db = fs_db,
                            onIngredientsSelected = {
                                // Handle Ingredients Tab action here
                                Log.d("RecipeDetail", "Ingredients Selected for ${it.label}")
                            },
                            onInstructionsSelected = {
                                // Handle Instructions Tab action here
                                Log.d("RecipeDetail", "Instructions Selected for ${it.label}")
                            }
                        )
                    }
                }
            }
        },
        bottomBar = { BottomNav(navController = navController) }
    )
}