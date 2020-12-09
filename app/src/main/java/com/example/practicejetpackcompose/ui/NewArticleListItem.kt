package com.example.practicejetpackcompose.ui

import com.example.practicejetpackcompose.model.ArticleDpo
import com.example.practicejetpackcompose.model.api.TagDto

sealed class NewArticleListItem {

    object Progress : NewArticleListItem()

    object Divider : NewArticleListItem()

    data class Tags(val tags: List<TagDto>) : NewArticleListItem()

    data class Article(val article: ArticleDpo) : NewArticleListItem()

    data class Error(val throwable: Throwable) : NewArticleListItem() {

        val message: String
            get() = throwable.localizedMessage.orEmpty()
    }
}