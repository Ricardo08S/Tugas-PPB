package com.newsapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newsapp.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    newsViewModel: NewsViewModel = viewModel()
) {
    val newsState by newsViewModel.newsState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = newsState) {
            is NewsState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is NewsState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.articles) { article ->
                        ArticleCard(
                            article = article,
                            onClick = {
                                navController.navigate(Screen.DetailScreen.createRoute(article.url))
                            }
                        )
                    }
                }
            }
            is NewsState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            }
        }
    }
}