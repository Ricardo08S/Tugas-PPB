package com.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.newsapp.auth.LoginScreen
import com.newsapp.auth.SignUpScreen
import com.newsapp.auth.ForgotPasswordScreen
import com.newsapp.ui.detail.DetailScreen
import com.newsapp.ui.main.MainScreen
import com.newsapp.ui.account.AccountScreen
import com.newsapp.ui.splash.SplashScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }
        composable(route = Screen.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(route = Screen.MainScreen.route) {
            MainScreen(mainNavController = navController)
        }
        composable(
            route = Screen.DetailScreen.route,
            arguments = listOf(navArgument("articleUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("articleUrl") ?: ""
            val decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
            DetailScreen(navController = navController, articleUrl = decodedUrl)
        }
        composable(route = Screen.AccountScreen.route) {
            AccountScreen(navController = navController)
        }
    }
}