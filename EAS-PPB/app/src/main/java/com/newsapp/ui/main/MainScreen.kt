package com.newsapp.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.material.icons.filled.AccountCircle
import com.newsapp.navigation.Screen
import com.newsapp.ui.home.HomeScreen
import com.newsapp.ui.search.SearchScreen


data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screen.HomeScreen.route),
        BottomNavItem("Search", Icons.Default.Search, Screen.SearchScreen.route)
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("News App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { mainNavController.navigate(Screen.AccountScreen.route) }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Account Settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            bottomNavController.navigate(screen.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.surfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.HomeScreen.route) { HomeScreen(navController = mainNavController) }
            composable(Screen.SearchScreen.route) { SearchScreen(navController = mainNavController) }
        }
    }
}