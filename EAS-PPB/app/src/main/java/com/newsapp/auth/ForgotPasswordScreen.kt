package com.newsapp.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newsapp.navigation.Screen

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Password reset email sent!", Toast.LENGTH_LONG).show()
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
                authViewModel.resetAuthState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, "Failed: ${state.message}", Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState()
            }
            else -> Unit
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Reset Your Password", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Enter your email to receive a password reset link.")
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            if (authState == AuthState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        if (email.isNotBlank()) {
                            authViewModel.sendPasswordReset(email)
                        } else {
                            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send Reset Link")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Back to Login",
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }
    }
}
