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
    private val tags = ConflatedBroadcastChannel<List<TagDto>>(emptyList())
    private val newArticles = ConflatedBroadcastChannel<List<ArticleDpo>>(emptyList())
    private val requestError = ConflatedBroadcastChannel<Throwable?>(null)

    private var nextPage = FIRST_PAGE

    var items: List<NewArticleListItem> by mutableStateOf(listOf())
        private set

    init {

        combine(
            isLoading.asFlow(),
            tags.asFlow(),
            newArticles.asFlow(),
            requestError.asFlow()
        ) { loading, tags, articles, error ->

            val progressItem = NewArticleListItem.Progress.takeIf { loading }
            val tagsItem = NewArticleListItem.Tags(tags = tags)
            val articleItems = articles
                .mapIndexed { index, articleDpo ->
                    listOfNotNull(
                        NewArticleListItem.Article(articleDpo),
                        NewArticleListItem.Divider.takeIf { index != articles.lastIndex }
                    )
                }
                .flatten()
            val errorItem = error?.let { NewArticleListItem.Error(it) }

            return@combine emptyList<NewArticleListItem>().asSequence()
                .plus(tagsItem)
                .plus(NewArticleListItem.Divider)
                .plus(articleItems)
                .plus(progressItem)
                .plus(errorItem)
                .filterNotNull()
                .toList()
        }
            .onEach { items = it }
            .launchIn(viewModelScope)
    }

    fun fetchFeed() {

        combine(
            articleRepository.getArticles(nextPage).toDpo().onEach { new ->
                nextPage++
                newArticles.send(newArticles.value + new)
            },
            tagRepository.getTags(FIRST_PAGE).onEach {
                tags.send(it)
            },
            // Fixme: 完了を通知したいだけなので[Unit]を返すようにしているが他に良い方法はないか...
            // RxJavaのCompletableのような機能があればそれを使いたい...
            transform = { _, _ -> Unit }
        ).fetch()
    }

    fun fetchNextArticles() {
        if (isLoading.value) return
        articleRepository.getArticles(nextPage).toDpo()
            .onEach { new ->
                nextPage++
                newArticles.send(newArticles.value + new)
            }
            .fetch()
    }

    private fun <T> Flow<T>.fetch() {
        onStart { isLoading.send(true); requestError.send(null) }
            .catch { requestError.send(it) }
            .onCompletion { isLoading.send(false) }
            .launchIn(viewModelScope)
    }
}
