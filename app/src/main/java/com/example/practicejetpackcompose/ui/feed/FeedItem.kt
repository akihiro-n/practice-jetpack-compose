package com.example.practicejetpackcompose.ui.feed

import com.example.practicejetpackcompose.model.ArticleDpo
import com.example.practicejetpackcompose.model.api.TagDto

sealed class FeedItem {

    object Progress : FeedItem()

    object Divider : FeedItem()

    data class Tags(val tags: List<TagDto>) : FeedItem()

    data class Article(val article: ArticleDpo) : FeedItem()

    data class Error(val throwable: Throwable) : FeedItem() {

        val message: String
            get() = throwable.localizedMessage.orEmpty()
    }
}