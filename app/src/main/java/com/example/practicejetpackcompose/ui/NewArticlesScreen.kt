package com.example.practicejetpackcompose.ui

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.example.practicejetpackcompose.model.ArticleDpo

@Composable
fun NewArticlesScreen() {
    val viewModel = viewModel<NewArticleViewModel>()
    NewArticleList(articles = viewModel.newArticles)
}

@Composable
fun NewArticleList(articles: List<ArticleDpo>) {
    LazyColumnFor(items = articles) { article ->
        Text(
            text = article.title,
            fontSize = 20.sp
        )
        Divider()
    }
}

@Preview
@Composable
fun NewArticlesScreenPreview() {
    val articles = (0..30).map { ArticleDpo(title = "Preview Test $it") }
    NewArticleList(articles = articles)
}