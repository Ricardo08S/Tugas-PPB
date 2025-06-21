package com.starbucksapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome!")
        Spacer(modifier = Modifier.height(8.dp))
        currentUser?.email?.let { email ->
            Text(text = "You are logged in as: $email")
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate("profile") }) {
            Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.padding(end = 8.dp))
            Text("View My Profile")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            auth.signOut()
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }
}