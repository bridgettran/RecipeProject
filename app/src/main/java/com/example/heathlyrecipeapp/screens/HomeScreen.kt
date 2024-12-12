package com.example.heathlyrecipeapp.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.heathlyrecipeapp.api.RecipesManager
import com.example.heathlyrecipeapp.api.model.Recipe
import androidx.navigation.NavController

/**
 * Displays the home screen of the app.
 *
 * The `HomeScreen` composable provides a list of recipes retrieved from the `RecipesManager` and
 * displays each recipe as a card. The user can click on a card to navigate to the recipe details.
 *
 * @param modifier The modifier to apply to the composable.
 * @param recipesManager The `RecipesManager` that fetches and holds the recipes data.
 * @param navController The navigation controller to handle navigation between screens.
 *
 * @return This composable does not return anything; it displays the list of recipes.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    recipesManager: RecipesManager,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF8F8F8)) // Light background
            .padding(16.dp) // Overall screen padding
    ) {
        // Fetch the list of recipes
        val recipes = recipesManager.recipesResponse.value

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp) // Spacing between items
        ) {
            items(recipes) { recipe ->
                RecipeCard(recipe = recipe, navController = navController)
            }
        }
    }
}

/**
 * Displays a single recipe card.
 *
 * The `RecipeCard` composable shows a recipe's image, title, and preparation time.
 * Clicking on the card navigates the user to the recipe detail screen.
 *
 * @param recipe The recipe data to display.
 * @param navController The navigation controller to handle navigation to the recipe detail screen.
 *
 * @return This composable does not return anything; it displays a recipe card UI.
 */
@Composable
fun RecipeCard(
    recipe: Recipe,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp)) // Card background
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)) // Elevation for shadow
            .clickable {
                // Navigate to recipe detail screen (adjust navigation logic as needed)
                navController.navigate("recipeDetail/${recipe.compositeKey}")
            }
    ) {
        Column {
            // Recipe image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.image)
                    .crossfade(true)
                    .build(),
                contentDescription = recipe.label,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)) // Rounded corners for the image
            )

            // Recipe details
            Column(
                modifier = Modifier.padding(12.dp) // Padding inside the card
            ) {
                Text(
                    text = recipe.label ?: "No title",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "${recipe.totalTime ?: 0} min",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
