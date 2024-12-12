/*
package com.example.heathlyrecipeapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.heathlyrecipeapp.screens.SignInScreen
import com.example.heathlyrecipeapp.ui.theme.HeathlyRecipeAppTheme

*/
/**
 * Activity for user sign-in.
 *
 * This activity initializes the UI for user authentication, including the login form.
 * It applies the app theme and uses Firebase Authentication to validate user credentials.
 *//*

class SignInActivity : ComponentActivity() {
    */
/**
     * Called when the activity is created.
     *
     * This method sets up the app's theme and the `SignInScreen` composable,
     * providing the application context to handle Firebase Authentication logic.
     *
     * @param savedInstanceState If non-null, this activity is being re-initialized after
     * previously being shut down.
     *//*

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            HeathlyRecipeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context: Context = applicationContext
                    SignInScreen(modifier = Modifier.padding(innerPadding), context = context)
                }
            }
        }
    }
}*/
package com.example.heathlyrecipeapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.heathlyrecipeapp.screens.AuthScreen
import com.example.heathlyrecipeapp.ui.theme.HeathlyRecipeAppTheme

/**
 * Activity for user authentication.
 *
 * This activity initializes the UI for user sign-in, registration, and password reset.
 * It uses Firebase Authentication to manage user credentials.
 */
class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeathlyRecipeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context: Context = applicationContext
                    AuthScreen(modifier = Modifier.padding(innerPadding), context = context)
                }
            }
        }
    }
}