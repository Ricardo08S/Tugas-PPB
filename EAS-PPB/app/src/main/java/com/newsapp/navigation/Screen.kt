package com.newsapp.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("signup_screen")
    object ForgotPasswordScreen : Screen("forgot_password_screen")
    object HomeScreen : Screen("home_screen")
    object MainScreen : Screen("main_screen")
    object SearchScreen : Screen("search_screen")
    object AccountScreen : Screen("account_screen")
    object DetailScreen : Screen("detail_screen/{articleUrl}") {
        fun createRoute(articleUrl: String): String {
            val encodedUrl = URLEncoder.encode(articleUrl, StandardCharsets.UTF_8.toString())
            return "detail_screen/$encodedUrl"
        }
    }
}