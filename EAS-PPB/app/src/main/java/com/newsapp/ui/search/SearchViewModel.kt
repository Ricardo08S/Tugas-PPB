package com.newsapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.BuildConfig
import com.newsapp.data.model.Article
import com.newsapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface SearchState {
    object Idle : SearchState
    object Loading : SearchState
    data class Success(val articles: List<Article>) : SearchState
    data class Error(val message: String) : SearchState
}

class SearchViewModel : ViewModel() {
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState

    private val apiKey = BuildConfig.NEWS_API_KEY

    fun searchNews(query: String) {
        if (query.isBlank()) {
            _searchState.value = SearchState.Idle
            return
        }
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            try {
                val response = RetrofitInstance.api.searchNews(query = query, apiKey = apiKey)
                val articlesWithImages = response.articles.filter { it.urlToImage != null }
                _searchState.value = SearchState.Success(articlesWithImages)
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(e.message ?: "An error occurred")
            }
        }
    }
}