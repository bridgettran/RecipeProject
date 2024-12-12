package com.example.heathlyrecipeapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.heathlyrecipeapp.api.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/**
 * Displays the Preferences Screen showing a list of saved recipes.
 *
 * The `PreferencesScreen` composable fetches recipes saved in the Firestore database
 * and displays them in a list. Users can click on a recipe to view its details or
 * edit the recipe's ingredients using a dialog.
 *
 * @param modifier The modifier to apply to the composable.
 * @param navController The navigation controller for handling navigation to other screens.
 *
 * @return This composable does not return anything but displays the list of saved recipes.
 */
@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var data by remember { mutableStateOf<List<Recipe>>(emptyList()) }
    var editingRecipe by remember { mutableStateOf<Recipe?>(null) }

    // Reference the recipes collection
    val collectionReference = FirebaseFirestore.getInstance().collection("recipes")

    // Fetch saved recipes
    LaunchedEffect(Unit) {
        collectionReference
            .get()
            .addOnSuccessListener { documents ->
                val dataList = documents.map { documentSnapshot ->
                    val dataMap = documentSnapshot.data
                    Recipe(
                        compositeKey = dataMap["recipe_compositeKey"] as String,
                        label = dataMap["recipe_label"] as? String,
                        calories = dataMap["recipe_calories"] as? Double,
                        totalTime = dataMap["recipe_totalTime"] as? Double,
                        image = dataMap["recipe_image"] as? String,
                        ingredientLines = dataMap["recipe_ingredientLines"] as? List<String>,
                        url = dataMap["recipe_url"] as? String,
                        isFavorite = dataMap["isFavorite"] as? Boolean
                    )
                }
                data = dataList
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }
    }

    // Edit Recipe Dialog
    if (editingRecipe != null) {
        EditRecipeDialog(
            recipe = editingRecipe!!,
            onDismiss = { editingRecipe = null },
            onSave = { updatedRecipe ->
                // Update Firestore document
                collectionReference.document(updatedRecipe.compositeKey)
                    .set(mapOf(
                        "recipe_compositeKey" to updatedRecipe.compositeKey,
                        "recipe_label" to updatedRecipe.label,
                        "recipe_calories" to updatedRecipe.calories,
                        "recipe_totalTime" to updatedRecipe.totalTime,
                        "recipe_image" to updatedRecipe.image,
                        "recipe_ingredientLines" to updatedRecipe.ingredientLines,
                        "recipe_url" to updatedRecipe.url,
                        "isFavorite" to updatedRecipe.isFavorite
                    ), SetOptions.merge()) // Use merge to update only specified fields
                    .addOnSuccessListener {
                        // Refresh the list
                        data = data.map { if (it.compositeKey == updatedRecipe.compositeKey) updatedRecipe else it }
                        editingRecipe = null
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Error updating recipe", exception)
                    }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF8F8F8))
            .padding(16.dp)
    ) {
        Text(
            text = "Saved Recipes",
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data) { recipe ->
                SavedRecipeCard(
                    recipe = recipe,
                    onEdit = { editingRecipe = it },
                    navController = navController
                )
            }
        }
    }
}

/**
 * Displays a card for each saved recipe.
 *
 * The `SavedRecipeCard` composable shows the recipe's image, title, and total time.
 * It also provides an edit button to modify the recipe's details.
 *
 * @param recipe The recipe data to display.
 * @param onEdit Callback invoked when the user clicks the edit button.
 * @param navController The navigation controller to navigate to the recipe detail screen.
 *
 * @return This composable does not return anything but displays a recipe card.
 */
@Composable
fun SavedRecipeCard(
    recipe: Recipe,
    onEdit: (Recipe) -> Unit,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
            .clickable {
                navController.navigate("recipeDetail/${recipe.compositeKey}")
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Recipe image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.image)
                    .crossfade(true)
                    .build(),
                contentDescription = recipe.label,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            )

            // Recipe details and edit button
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween // Adjusts spacing
            ) {
                Column(
                    modifier = Modifier.weight(0.8f) // Adjust weight for proportional space
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

                // Edit button with proper alignment
                IconButton(
                    modifier = Modifier.padding(end = 8.dp), // Add padding to prevent edge clipping
                    onClick = { onEdit(recipe) }
                ) {
                    /*Icon(Icons.Default.Edit, contentDescription = "Edit Recipe")*/
                    Icon(Icons.Default.Edit, contentDescription = "Edit Recipe: ${recipe.label}")
                }
            }
        }
    }
}


// The EditRecipeDialog remains the same as in the previous implementation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeDialog(
    recipe: Recipe,
    onDismiss: () -> Unit,
    onSave: (Recipe) -> Unit
) {
    // State to manage the list of ingredients with their quantities
    var editedIngredients by remember {
        mutableStateOf(
            recipe.ingredientLines?.map { ingredientLine ->
                // More robust parsing of ingredient line
                val parts = ingredientLine.split(" ", limit = 2)
                val quantity = parts.firstOrNull()?.toIntOrNull()
                val name = if (quantity != null) parts.getOrNull(1) ?: "" else ingredientLine
                name to quantity
            }?.toMutableList() ?: mutableListOf()
        )
    }

    var newIngredientName by remember { mutableStateOf("") }
    var newIngredientQuantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Recipe") },
        text = {
            Column {
                // Ingredient List with Editable Quantities
                Text("Ingredients:", style = MaterialTheme.typography.titleMedium)
                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(editedIngredients) { index, ingredient ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Quantity Input
                            TextField(
                                value = ingredient.second?.toString() ?: "",
                                onValueChange = { newQuantity ->
                                    editedIngredients = editedIngredients.toMutableList().apply {
                                        this[index] = ingredient.first to newQuantity.toIntOrNull()
                                    }
                                },
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(end = 8.dp),
                                placeholder = { Text("Qty") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            // Ingredient Name
                            TextField(
                                value = ingredient.first,
                                onValueChange = { newName ->
                                    editedIngredients = editedIngredients.toMutableList().apply {
                                        this[index] = newName to ingredient.second
                                    }
                                },
                                modifier = Modifier
                                    .weight(0.6f)
                                    .padding(end = 8.dp),
                                placeholder = { Text("Ingredient") },
                                singleLine = true
                            )

                            // Delete Ingredient Button
                            IconButton(
                                onClick = {
                                    editedIngredients = editedIngredients.toMutableList().apply {
                                        removeAt(index)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Ingredient")
                            }
                        }
                    }
                }

                // Add New Ingredient Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newIngredientQuantity,
                        onValueChange = { newIngredientQuantity = it },
                        modifier = Modifier
                            .weight(0.3f)
                            .padding(end = 8.dp),
                        placeholder = { Text("Qty") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    TextField(
                        value = newIngredientName,
                        onValueChange = { newIngredientName = it },
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(end = 8.dp),
                        placeholder = { Text("New Ingredient") },
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (newIngredientName.isNotBlank() && newIngredientQuantity.toIntOrNull() != null) {
                                editedIngredients.add(newIngredientName to newIngredientQuantity.toInt())
                                newIngredientName = ""
                                newIngredientQuantity = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Ingredient")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Reconstruct the ingredient list to save
                    val updatedIngredientLines = editedIngredients.map { ingredient ->
                        "${ingredient.second ?: ""} ${ingredient.first}".trim()
                    }
                    val updatedRecipe = recipe.copy(ingredientLines = updatedIngredientLines)
                    onSave(updatedRecipe)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}