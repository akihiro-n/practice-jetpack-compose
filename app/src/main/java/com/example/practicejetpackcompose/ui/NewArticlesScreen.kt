package com.example.practicejetpackcompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.example.practicejetpackcompose.model.ArticleDpo
import com.example.practicejetpackcompose.util.bitmapRequestCallback
import com.squareup.picasso.Picasso

@Composable
fun NewArticlesScreen() {
    val viewModel = viewModel<NewArticleViewModel>()
    NewArticleList(articles = viewModel.newArticles)
}

@Composable
fun NewArticleList(articles: List<ArticleDpo>) {
    val picasso = Picasso.get()
    LazyColumnFor(items = articles) { article ->
        var imageProfileImageAsset by remember { mutableStateOf<ImageAsset?>(null) }
        Row {
            Card(shape = CircleShape) {
                Image(asset = imageProfileImageAsset ?: return@Card)
            }
            Text(
                text = article.title,
                fontSize = 20.sp
            )
        }
        Divider()
        val profileImageUrl = article.profileImageUrl
        val callback = bitmapRequestCallback(
            onSuccess = { imageProfileImageAsset = it.asImageAsset() }
        )
        onCommit(profileImageUrl) { picasso.load(profileImageUrl).into(callback) }
        onDispose { picasso.cancelRequest(callback) }
    }
}

@Preview
@Composable
fun NewArticlesScreenPreview() {
    val articles = (0..30).map { ArticleDpo(title = "Preview Test $it") }
    NewArticleList(articles = articles)
}