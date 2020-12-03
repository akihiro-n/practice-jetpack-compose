package com.example.practicejetpackcompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.example.practicejetpackcompose.model.ArticleDpo
import com.example.practicejetpackcompose.util.getBitmapImage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

@Composable
fun NewArticlesScreen() {
    val viewModel = viewModel<NewArticleViewModel>()
    NewArticleList(articles = viewModel.newArticles)
}

@Composable
fun NewArticleList(articles: List<ArticleDpo>) {

    LazyColumnFor(items = articles) { article ->

        var profileImage by remember(article.profileImageUrl) {
            mutableStateOf<ImageAsset?>(null)
        }
        rememberCoroutineScope().launch {
            profileImage = Picasso.get().getBitmapImage(article.profileImageUrl).asImageAsset()
        }

        Card {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier.width(48.dp).height(48.dp)
                ) profileImage@{
                    Image(asset = profileImage ?: return@profileImage)
                }
                Text(
                    text = article.title,
                    fontSize = 20.sp
                )
            }
        }
        Divider()
    }
}

@Preview
@Composable
fun NewArticlesScreenPreview() {
    val articles = (0..30).map { ArticleDpo(title = "Preview Test $it") }
    NewArticleList(articles = articles)
}