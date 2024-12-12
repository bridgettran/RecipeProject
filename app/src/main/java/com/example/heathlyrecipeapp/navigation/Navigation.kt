package com.example.heathlyrecipeapp.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.heathlyrecipeapp.R
import com.example.heathlyrecipeapp.destinations.Destination

/**
 * Displays the bottom navigation bar for the app.
 *
 * @param navController The NavController used to handle navigation between screens.
 */
@Composable
fun BottomNav(navController: NavController){
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        val ic_home = painterResource(id = R.drawable.ic_home)
        val ic_search = painterResource(id = R.drawable.ic_search)
        val ic_preferences = painterResource(id = R.drawable.ic_preferences)

        // Home Screen Navigation Item
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Home.route,
            onClick = { navController.navigate(Destination.Home.route) {
                popUpTo(Destination.Home.route)
                launchSingleTop = true
            }},
            icon = { Icon(painter = ic_home, contentDescription = "Home Screen icon") },
            label = {
                Text(text = Destination.Home.route)
            }
        )

        // Search Screen Navigation Item
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Search.route,
            onClick = { navController.navigate(Destination.Search.route) {
                popUpTo(Destination.Search.route)
                launchSingleTop = true
            }},
            icon = { Icon(painter = ic_search, contentDescription = "Barcode Search Screen icon") },
            label = {
                Text(text = Destination.Search.route)
            }
        )

        // Preferences Screen Navigation Item
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Preferences.route,
            onClick = { navController.navigate(Destination.Preferences.route) {
                popUpTo(Destination.Preferences.route)
                launchSingleTop = true
            }},
            icon = { Icon(painter = ic_preferences, contentDescription = "Diet Preferences Screen icon") },
            label = {
                Text(text = Destination.Preferences.route)
            }
        )
    }
}
