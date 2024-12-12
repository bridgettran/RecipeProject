package com.example.heathlyrecipeapp.screens

import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.heathlyrecipeapp.api.model.Recipe
import com.example.heathlyrecipeapp.db.AppDatabase
import com.example.heathlyrecipeapp.mvvm.RecipeViewModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Displays detailed information about a specific recipe, including ingredients, instructions,
 * and the option to mark it as a favorite.
 *
 * @param recipe The recipe to display.
 * @param modifier Modifier to customize the layout.
 * @param viewModel The ViewModel managing recipe data and interactions.
 * @param database The local database instance for recipe data management.
 * @param fs_db The Firebase Firestore instance for syncing recipe data.
 * @param onIngredientsSelected Callback triggered when the "Ingredients" tab is selected.
 * @param onInstructionsSelected Callback triggered when the "Instructions" tab is selected.
 */
@Composable
fun RecipeDetailScreen(
    recipe: Recipe,
    modifier: Modifier,
    viewModel: RecipeViewModel,
    database: AppDatabase,
    fs_db: FirebaseFirestore,
    onIngredientsSelected: () -> Unit, // Callback for Ingredients
    onInstructionsSelected: () -> Unit // Callback for Instructions
) {
    var selectedTab by remember { mutableStateOf("Ingredients") }
    val isIconChanged = viewModel.recipeIconState.value[recipe.compositeKey] ?: false
    var lastInsertedDocument by remember { mutableStateOf<DocumentReference?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            // Top Section with Image and Favorite Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(top = 60.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(recipe.image)
                        .build(),
                    /*contentDescription = recipe.label,*/
                    contentDescription = "${recipe.label}, a recipe that takes ${recipe.totalTime ?: 0} minutes to cook.",
                    contentScale = ContentScale.Crop
                )

                /*Button(
                    onClick = {
                        val iconState = !isIconChanged
                        viewModel.updateRecipeIconState(recipe.compositeKey!!, database)
                        handleFirestoreUpdate(
                            recipe = recipe.copy(isFavorite = iconState), // Use copy to create a new instance
                            isIconChanged = iconState,
                            fs_db = fs_db,
                            lastInsertedDocument = lastInsertedDocument,
                            setLastInsertedDocument = { lastInsertedDocument = it }
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        imageVector = if (isIconChanged) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }*/

                Button(
                    onClick = {
                        val iconState = !isIconChanged
                        viewModel.updateRecipeIconState(recipe.compositeKey!!, database)
                        handleFirestoreUpdate(
                            recipe = recipe.copy(isFavorite = iconState),
                            isIconChanged = iconState,
                            fs_db = fs_db,
                            lastInsertedDocument = lastInsertedDocument,
                            setLastInsertedDocument = { lastInsertedDocument = it }
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.7f)) // Add semi-transparent white background
                        .padding(8.dp), // Inner padding for the button
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        imageVector = if (isIconChanged) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }

            }

            // Recipe Title
            recipe.label?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Calories and Time Section
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                recipe.calories?.let {
                    Text(
                        text = "Calories: $it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                recipe.totalTime?.let {
                    Text(
                        text = "Time: $it min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TabItem(
                    title = "Ingredients",
                    isSelected = selectedTab == "Ingredients",
                    onClick = {
                        selectedTab = "Ingredients"
                        onIngredientsSelected()
                    }
                )
                TabItem(
                    title = "Instructions",
                    isSelected = selectedTab == "Instructions",
                    onClick = {
                        selectedTab = "Instructions"
                        onInstructionsSelected()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Tab Content
            when (selectedTab) {
                "Ingredients" -> IngredientsList(recipe.ingredientLines)
                "Instructions" -> InstructionsView(recipe.url)
            }
        }
    }
}

// Firestore update helper function
private fun handleFirestoreUpdate(
    recipe: Recipe,
    isIconChanged: Boolean,
    fs_db: FirebaseFirestore,
    lastInsertedDocument: DocumentReference?,
    setLastInsertedDocument: (DocumentReference?) -> Unit
) {
    val collection = fs_db.collection("recipes")
    val recipeMap = mapOf(
        "recipe_compositeKey" to recipe.compositeKey,
        "recipe_label" to recipe.label,
        "recipe_calories" to recipe.calories,
        "recipe_totalTime" to recipe.totalTime,
        "recipe_image" to recipe.image,
        "recipe_ingredientLines" to recipe.ingredientLines,
        "recipe_url" to recipe.url,
        "isFavorite" to isIconChanged
    )

    GlobalScope.launch {
        try {
            // Check if recipe already exists in the database
            val existingRecipeQuery = collection
                .whereEqualTo("recipe_compositeKey", recipe.compositeKey)
                .get()
                .await()

            if (existingRecipeQuery.isEmpty) {
                // If recipe doesn't exist and is being favorited, add it
                if (isIconChanged) {
                    collection.document(recipe.compositeKey).set(recipeMap).await()
                    setLastInsertedDocument(null)
                    Log.d("Firestore", "Recipe saved successfully")
                }
            } else {
                val existingDocumentRef = existingRecipeQuery.documents[0].reference

                if (isIconChanged) {
                    // If favorite is true, update the existing document
                    existingDocumentRef.update(recipeMap)
                    Log.d("Firestore", "Recipe updated successfully")
                } else {
                    // If favorite is false, delete the document
                    existingDocumentRef.delete().await()
                    Log.d("Firestore", "Recipe removed from favorites")
                }
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error updating recipe: ${e.message}")
        }
    }
}

// TabItem Composable (Modern Design)
@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                shape = MaterialTheme.shapes.small
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

// Ingredients List (Modern UI)
@Composable
fun IngredientsList(ingredients: List<String>?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ingredients?.forEach { ingredient ->
            Text(
                text = ingredient,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

// Instructions WebView Embedded in Instructions Tab
@Composable
fun InstructionsView(url: String?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    loadUrl(url ?: "https://example.com") // Load URL from recipe
                }
            }
        )
    }
}

// a check to see which movies already exist in firebase db
suspend fun doesRecipeExist(recipe_compositeKey: String, collection: CollectionReference): Boolean{
    val querySnapshot = collection.whereEqualTo("recipe_compositeKey", recipe_compositeKey).get().await()
    return !querySnapshot.isEmpty
}