package com.newsapp.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newsapp.R
import com.newsapp.navigation.Screen

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.MainScreen.route) {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
                authViewModel.resetAuthState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, "Login Failed: ${state.message}", Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState()
            }
            else -> Unit
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Welcome Back!", style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = "Login to your account",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Forgot Password?",
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { navController.navigate(Screen.ForgotPasswordScreen.route) },
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                if (authState == AuthState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                authViewModel.loginUser(email, password)
                            } else {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Login", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            Text(
                text = "Don't have an account? Sign Up",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .clickable { navController.navigate(Screen.SignUpScreen.route) },
                textAlign = TextAlign.Center
            )
        }
    }
}