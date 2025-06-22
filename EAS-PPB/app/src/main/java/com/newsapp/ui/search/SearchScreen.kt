package com.newsapp.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newsapp.navigation.Screen
import com.newsapp.ui.home.ArticleCard

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel()
) {
    val searchState by searchViewModel.searchState.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search News...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    searchViewModel.searchNews(query)
                    keyboardController?.hide()
                }
            )
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = searchState) {
                is SearchState.Idle -> {
                    Text("Search for articles above", modifier = Modifier.align(Alignment.Center))
                }
                is SearchState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is SearchState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                is SearchState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
            }
        }
    }
}