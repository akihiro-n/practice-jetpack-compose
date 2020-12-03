package com.example.practicejetpackcompose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicejetpackcompose.data.ArticleRepository
import com.example.practicejetpackcompose.model.ArticleDpo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NewArticleViewModel @ViewModelInject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    private val isLoading: Channel<Boolean> = Channel(Channel.CONFLATED)
    private val newArticles: Channel<List<ArticleDpo>> = Channel(Channel.CONFLATED)
    private val requestError: Channel<Throwable?> = Channel(Channel.CONFLATED)

    var items: List<NewArticleListItem> by mutableStateOf(listOf())
        private set

    init {

        viewModelScope.launch {
            isLoading.send(false)
            newArticles.send(emptyList())
            requestError.send(null)
        }

        combine(
            isLoading.receiveAsFlow(),
            newArticles.receiveAsFlow(),
            requestError.receiveAsFlow()
        ) { loading, articles, error ->

            val progressItem = NewArticleListItem.Progress.takeIf { loading }
            val articleItems = articles.map { NewArticleListItem.Article(it) }
            val errorItem = error?.let { NewArticleListItem.Error(it) }

            return@combine emptyList<NewArticleListItem>()
                .plus(progressItem)
                .plus(articleItems)
                .plus(errorItem)
                .filterNotNull()
        }
            .onEach { items = it }
            .launchIn(viewModelScope)
    }

    fun fetchNewArticles() = repository.getArticles().map { it.map(::ArticleDpo) }
        .onStart {
            isLoading.send(true)
            requestError.send(null)
        }
        .onEach { newArticles.send(it) }
        .catch { requestError.send(it) }
        .onCompletion { isLoading.send(false) }
        .launchIn(viewModelScope)
}