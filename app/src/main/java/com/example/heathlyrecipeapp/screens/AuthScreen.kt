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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.heathlyrecipeapp.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

/**
 * Composable screen for user authentication with sign-in, registration, and password reset.
 *
 * @param context The application context for performing authentication and navigation.
 * @param modifier The modifier to apply to the composable.
 */
@Composable
fun AuthScreen(context: Context, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }
    var isResetPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = when {
                isRegistering -> "Create Account"
                isResetPassword -> "Reset Password"
                else -> "Sign In"
            },
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display Name field (only for registration)
        if (isRegistering) {
            TextField(
                value = displayName,
                onValueChange = { displayName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Display Name") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )
        }

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        // Password TextField (only for sign in and registration)
        if (!isResetPassword) {
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
        }

        // Error message
        if (error != null) {
            Text(
                text = error!!,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Main Action Button
        Button(
            onClick = {
                keyboardController?.hide()
                when {
                    isRegistering -> performRegistration(
                        email,
                        password,
                        displayName,
                        context,
                        onError = { errorMsg -> error = errorMsg }
                    )
                    isResetPassword -> performPasswordReset(
                        email,
                        context,
                        onError = { errorMsg -> error = errorMsg }
                    )
                    else -> performSignIn(
                        email,
                        password,
                        context,
                        onError = { errorMsg -> error = errorMsg }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = when {
                    isRegistering -> "Register"
                    isResetPassword -> "Reset Password"
                    else -> "Sign In"
                }
            )
        }

        // Toggle buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Switch between Sign In and Register
            Button(
                onClick = {
                    isRegistering = !isRegistering
                    isResetPassword = false
                    error = null
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = if (isRegistering) "Back to Sign In"
                    else "Create New Account"
                )
            }

            // Password Reset Toggle
            Button(
                onClick = {
                    isResetPassword = !isResetPassword
                    isRegistering = false
                    error = null
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = if (isResetPassword) "Back to Sign In"
                    else "Forgot Password?"
                )
            }
        }
    }
}

/**
 * Handles user registration with Firebase Authentication.
 *
 * @param email User's email address
 * @param password User's password
 * @param displayName User's display name
 * @param context Application context
 * @param onError Callback to handle registration errors
 */
private fun performRegistration(
    email: String,
    password: String,
    displayName: String,
    context: Context,
    onError: (String) -> Unit
) {
    if (email.isEmpty() || password.isEmpty() || displayName.isEmpty()) {
        onError("Please fill in all fields")
        return
    }

    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Update user profile with display name
                val user = auth.currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()

                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { profileTask ->
                        if (profileTask.isSuccessful) {
                            Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                            navigateToMainActivity(context)
                        } else {
                            onError("Profile update failed")
                        }
                    }
            } else {
                onError(task.exception?.message ?: "Registration Failed")
            }
        }
}

/**
 * Handles user sign-in with Firebase Authentication.
 *
 * @param email User's email address
 * @param password User's password
 * @param context Application context
 * @param onError Callback to handle sign-in errors
 */
private fun performSignIn(
    email: String,
    password: String,
    context: Context,
    onError: (String) -> Unit
) {
    if (email.isEmpty() || password.isEmpty()) {
        onError("Please enter email and password")
        return
    }

    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Sign In Successful", Toast.LENGTH_SHORT).show()
                navigateToMainActivity(context)
            } else {
                onError(task.exception?.message ?: "Sign In Failed")
            }
        }
}

/**
 * Handles password reset via Firebase Authentication.
 *
 * @param email User's email address
 * @param context Application context
 * @param onError Callback to handle password reset errors
 */
private fun performPasswordReset(
    email: String,
    context: Context,
    onError: (String) -> Unit
) {
    if (email.isEmpty()) {
        onError("Please enter your email")
        return
    }

    val auth = FirebaseAuth.getInstance()
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Password Reset Email Sent", Toast.LENGTH_SHORT).show()
            } else {
                onError(task.exception?.message ?: "Password Reset Failed")
            }
        }
}

/**
 * Navigates to the MainActivity after successful authentication.
 *
 * @param context Application context
 */
private fun navigateToMainActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
}

