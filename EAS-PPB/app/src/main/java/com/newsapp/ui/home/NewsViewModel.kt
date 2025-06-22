package com.newsapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.BuildConfig
import com.newsapp.data.model.Article
import com.newsapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface NewsState {
    object Loading : NewsState
    data class Success(val articles: List<Article>) : NewsState
    data class Error(val message: String) : NewsState
}

class NewsViewModel : ViewModel() {
    private val _newsState = MutableStateFlow<NewsState>(NewsState.Loading)
    val newsState: StateFlow<NewsState> = _newsState

    private val apiKey = BuildConfig.NEWS_API_KEY

    init {
        getTopHeadlines()
    }

    private fun getTopHeadlines() {
        viewModelScope.launch {
            _newsState.value = NewsState.Loading
            try {
                val response = RetrofitInstance.api.getTopHeadlines(apiKey = apiKey)
                val articlesWithImages = response.articles.filter { it.urlToImage != null }
                _newsState.value = NewsState.Success(articlesWithImages)
            } catch (e: Exception) {
                _newsState.value = NewsState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}