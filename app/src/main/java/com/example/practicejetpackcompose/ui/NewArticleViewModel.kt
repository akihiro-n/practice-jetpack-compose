package com.example.practicejetpackcompose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicejetpackcompose.data.ArticleRepository
import com.example.practicejetpackcompose.data.TagRepository
import com.example.practicejetpackcompose.domain.toDpo
import com.example.practicejetpackcompose.model.ArticleDpo
import com.example.practicejetpackcompose.model.api.TagDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
class NewArticleViewModel @ViewModelInject constructor(
    private val articleRepository: ArticleRepository,
    private val tagRepository: TagRepository
) : ViewModel() {

    companion object {
        private const val FIRST_PAGE = 1L
    }

    private val isLoading = ConflatedBroadcastChannel(false)
    private val newArticles = ConflatedBroadcastChannel<List<ArticleDpo>>(emptyList())
    private val requestError = ConflatedBroadcastChannel<Throwable?>(null)

    private var nextPage = FIRST_PAGE

    var items: List<NewArticleListItem> by mutableStateOf(listOf())
        private set

    init {

        combine(
            isLoading.asFlow(),
            newArticles.asFlow(),
            requestError.asFlow()
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

    // TODO: タグ一覧と記事一覧を取得する
    fun fetchFeed() {
        merge(fetchArticles(), fetchTags()).fetch()
    }

    fun fetchNextArticles() {
        if (isLoading.value) return
        fetchArticles().fetch()
    }

    private fun fetchTags(): Flow<List<TagDto>> =
        tagRepository.getTags(FIRST_PAGE)

    private fun fetchArticles(): Flow<List<ArticleDpo>> =
        articleRepository.getArticles(nextPage)
            .toDpo()
            .onEach { new ->
                nextPage++
                newArticles.send(newArticles.value + new)
            }

    private fun <T> Flow<T>.fetch() {
        onStart { isLoading.send(true); requestError.send(null) }
            .catch { requestError.send(it) }
            .onCompletion { isLoading.send(false) }
            .launchIn(viewModelScope)
    }
}
