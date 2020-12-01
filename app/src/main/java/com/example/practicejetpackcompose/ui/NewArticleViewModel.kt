package com.example.practicejetpackcompose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicejetpackcompose.data.ArticleRepository
import com.example.practicejetpackcompose.model.ArticleDpo
import kotlinx.coroutines.flow.*

class NewArticleViewModel @ViewModelInject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    var newArticles: List<ArticleDpo> by mutableStateOf(listOf())
        private set

    fun fetchNewArticles() = repository.getArticles()
        .onEach { newArticles = newArticles + it.map(::ArticleDpo) }
        .catch { /** TODO: エラーハンドリング */ }
        .launchIn(viewModelScope)
}