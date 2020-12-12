package com.example.practicejetpackcompose.ui.feed

import androidx.compose.runtime.*
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
import kotlinx.coroutines.flow.*

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModel @ViewModelInject constructor(
    private val articleRepository: ArticleRepository,
    private val tagRepository: TagRepository
) : ViewModel() {

    companion object {
        private const val FIRST_PAGE = 1L
    }

    private val isLoading = MutableStateFlow(false)
    private val tags = MutableStateFlow<List<TagDto>>(emptyList())
    private val newArticles = MutableStateFlow<List<ArticleDpo>>(emptyList())
    private val requestError = MutableStateFlow<Throwable?>(null)

    /** 記事一覧の次ページのIndex */
    private var nextPage = FIRST_PAGE

    var items: List<FeedItem> by mutableStateOf(emptyList())
        private set

    init {

        /** Feed画面に表示するデータをマージする */
        combine(isLoading, tags, newArticles, requestError) { loading, tags, articles, error ->

            val progressItem = FeedItem.Progress.takeIf { loading }
            val tagsItem = FeedItem.Tags(tags = tags)
            val articleItems = articles
                .mapIndexed { index, articleDpo ->
                    listOfNotNull(
                        FeedItem.Article(articleDpo),
                        FeedItem.Divider.takeIf { index != articles.lastIndex }
                    )
                }
                .flatten()
            val errorItem = error?.let { FeedItem.Error(it) }

            return@combine emptyList<FeedItem>().asSequence()
                .plus(tagsItem)
                .plus(FeedItem.Divider)
                .plus(articleItems)
                .plus(progressItem)
                .plus(errorItem)
                .filterNotNull()
                .toList()
        }
            .onEach { items = it }
            .launchIn(viewModelScope)
    }

    /** タグ一覧と最新記事一覧をリクエスト */
    fun fetchFeed() {
        combine(
            articleRepository.getArticles(nextPage).toDpo().onEach { new ->
                nextPage++
                newArticles.value = newArticles.value + new
            },
            tagRepository.getTags(FIRST_PAGE).onEach {
                tags.value = it
            },
            // Fixme: 完了を通知したいだけなので[Unit]を返すようにしているが他に良い方法はないか...
            // RxJavaのCompletableのような機能があればそれを使いたい...
            transform = { _, _ -> Unit }
        ).fetch()
    }

    /** 記事一覧のページング */
    fun fetchNextArticles() {
        if (isLoading.value) return
        articleRepository.getArticles(nextPage).toDpo()
            .onEach { new ->
                nextPage++
                newArticles.value = newArticles.value + new
            }
            .fetch()
    }

    /** HTTPリクエスト次のローディングとエラーの状態を管理 */
    private fun <T> Flow<T>.fetch() {
        onStart {
            isLoading.value = true
            requestError.value = null
        }
            .catch { requestError.value = it }
            .onCompletion { isLoading.value = false }
            .launchIn(viewModelScope)
    }
}
