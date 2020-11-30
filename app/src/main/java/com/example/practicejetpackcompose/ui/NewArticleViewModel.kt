package com.example.practicejetpackcompose.ui

import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicejetpackcompose.data.ArticleRepository
import com.example.practicejetpackcompose.model.ArticleDpo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NewArticleViewModel @ViewModelInject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    var newArticles: List<ArticleDpo> by mutableStateOf(emptyList())
        private set

    fun fetchNewArticles() {
        viewModelScope.launch {
            repository.getArticles()
                .map { it.map(::ArticleDpo) }
                .collect {
                    newArticles = newArticles + it
                }
        }
    }
}