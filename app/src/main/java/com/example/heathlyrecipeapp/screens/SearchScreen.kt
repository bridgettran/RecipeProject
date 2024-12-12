package com.example.heathlyrecipeapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heathlyrecipeapp.db.AppDatabase
import com.example.heathlyrecipeapp.mvvm.RecipeViewModel

/**
 * Displays the search screen where users can search for recipes by name or keyword.
 *
 * @param modifier Modifier to customize the layout.
 * @param viewModel The ViewModel managing recipe data and interactions.
 * @param database The local database instance for recipe data management.
 * @param navController The NavController used for navigation to other screens.
 */
/*
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: RecipeViewModel,
    database: AppDatabase,
    navController: NavController
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var query by rememberSaveable { viewModel.searchTerm }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top AppBar Style Header
        Text(
            text = "Search Recipes",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Search Bar Section
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.searchTerm.value = it
            },
            label = { Text("Search for a recipe") },
            placeholder = { Text("Enter a recipe name or keyword") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                viewModel.searchRecipes(query, database)
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Search and Clear Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    viewModel.searchRecipes(query, database)
                    keyboardController?.hide()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    query = ""
                    viewModel.searchTerm.value = ""
                    viewModel.searchRecipes(query, database)
                    keyboardController?.hide()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear")
            }
        }

        // Divider for separation
        Spacer(modifier = Modifier.height(16.dp))

        // Recipe Results Section
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            if (viewModel.recipes.value.isEmpty()) {
                item {
                    Text(
                        text = "No recipes found. Try searching for something else.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                }
            } else {
                items(viewModel.recipes.value) { recipe ->
                    RecipeCard(recipe, navController = navController)
                }
            }
        }
    }
}
*/

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: RecipeViewModel,
    database: AppDatabase,
    navController: NavController
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var query by rememberSaveable { viewModel.searchTerm }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedFilter by rememberSaveable { mutableStateOf("All") }

    val healthFilters = listOf("All", "Vegan", "Gluten-Free", "Low-Sugar", "Paleo")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top AppBar Style Header
        Text(
            text = "Search Recipes",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Search Bar Section
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.searchTerm.value = it
            },
            label = { Text("Search for a recipe") },
            placeholder = { Text("Enter a recipe name or keyword") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                viewModel.searchRecipes(query, selectedFilter, database)
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Filter Dropdown Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Filter by: $selectedFilter",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(8.dp)
            )
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Filter"
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            healthFilters.forEach { filter ->
                DropdownMenuItem(
                    text = { Text(filter) },
                    onClick = {
                        selectedFilter = filter
                        expanded = false
                    }
                )
            }
        }

        // Search and Clear Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    viewModel.searchRecipes(query, selectedFilter, database)
                    keyboardController?.hide()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    query = ""
                    selectedFilter = "All"
                    viewModel.searchTerm.value = ""
                    viewModel.searchRecipes(query, selectedFilter, database)
                    keyboardController?.hide()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear")
            }
        }

        // Divider for separation
        Spacer(modifier = Modifier.height(16.dp))

        // Recipe Results Section
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            if (viewModel.recipes.value.isEmpty()) {
                item {
                    Text(
                        text = "No recipes found. Try searching for something else.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                }
            } else {
                items(viewModel.recipes.value) { recipe ->
                    RecipeCard(recipe, navController = navController)
                }
            }
        }
    }
}
