package com.example.practicejetpackcompose.ui

import com.example.practicejetpackcompose.model.ArticleDpo

sealed class NewArticleListItem {
    object Progress : NewArticleListItem()

    data class Article(val article: ArticleDpo) : NewArticleListItem()

    data class Error(val throwable: Throwable) : NewArticleListItem() {
        val message: String
            get() = throwable.localizedMessage.orEmpty()
    }
}