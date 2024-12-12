package com.example.heathlyrecipeapp.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.heathlyrecipeapp.MainActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Composable screen for user sign-in.
 *
 * This composable provides a user interface for logging into the app using an email and password.
 * It uses Firebase Authentication to validate credentials and navigate to the main application upon success.
 *
 * @param context The application context for performing authentication and navigation.
 * @param modifier The modifier to apply to the composable.
 *
 * @return This composable does not return anything, but it handles user interaction for login.
 */
@Composable
fun SignInScreen(context: Context, modifier: Modifier = Modifier){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        if (error != null) {
            Text(
                text = error!!,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Button(
            onClick = {
                // Perform Firebase authentication
                if (keyboardController != null) {
                    performSignIn(email, password, context, keyboardController)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Sign In")
        }
    }
}


/**
 * Handles the sign-in process using Firebase Authentication.
 *
 * @param context The application context for navigation.
 * @param email The user's email address.
 * @param password The user's password.
 * @param onError Callback invoked with an error message if sign-in fails.
 */
private fun performSignIn(
    email: String,
    password: String,
    context: Context,
    keyboardController: SoftwareKeyboardController?
){
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email,password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(context, "Sign In Successful", Toast.LENGTH_SHORT).show()
                var intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("userID", FirebaseAuth.getInstance().currentUser?.email)
                context.startActivity(intent)
            }else{
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
            }
            keyboardController?.hide()
        }
}