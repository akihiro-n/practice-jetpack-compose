package com.example.practicejetpackcompose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicejetpackcompose.data.ArticleRepository
import com.example.practicejetpackcompose.model.ArticleDpo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class NewArticleViewModel @ViewModelInject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    companion object {
        private const val FIRST_PAGE = 1L
    }

    private val isLoading: Channel<Boolean> = Channel(Channel.CONFLATED)
    private val newArticles: Channel<List<ArticleDpo>> = Channel(Channel.CONFLATED)
    private val requestError: Channel<Throwable?> = Channel(Channel.CONFLATED)

    var items: List<NewArticleListItem> by mutableStateOf(listOf())
        private set

    init {
        isLoading.offer(false)
        newArticles.offer(emptyList())
        requestError.offer(null)

        combine(
            isLoading.receiveAsFlow(),
            newArticles.receiveAsFlow(),
            requestError.receiveAsFlow()
        ) { loading, articles, error ->

            val progressItem = NewArticleListItem.Progress.takeIf { loading }
            val articleItems = articles
                .mapIndexed { index, articleDpo ->
                    listOfNotNull(
                        NewArticleListItem.Article(articleDpo),
                        NewArticleListItem.Divider.takeIf { index != articles.lastIndex }
                    )
                }
                .flatten()
            val errorItem = error?.let { NewArticleListItem.Error(it) }

            return@combine articleItems
                .plus(progressItem)
                .plus(errorItem)
                .filterNotNull()
        }
            .onEach { items = it }
            .launchIn(viewModelScope)
    }

    fun fetchNewArticles() {
        repository.getArticles(page = FIRST_PAGE)
            .map { it.map { dto -> ArticleDpo(dto = dto) } }
            .onStart {
                isLoading.send(true)
                requestError.send(null)
            }
            .onEach { newArticles.send(it) }
            .catch { requestError.send(it) }
            .onCompletion { isLoading.send(false) }
            .launchIn(viewModelScope)
    }
}