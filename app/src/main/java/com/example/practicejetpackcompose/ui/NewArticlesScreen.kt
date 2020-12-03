package com.example.practicejetpackcompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
    NewArticleList(items = viewModel.items)
}

@Composable
fun NewArticleList(items: List<NewArticleListItem>) {
    LazyColumnFor(items) { item ->
        when (item) {
            is NewArticleListItem.Progress -> ProgressColumn()
            is NewArticleListItem.Error -> ErrorMessageColumn(item)
            is NewArticleListItem.Article -> NewArticleColumn(item)
        }
    }
}

@Composable
fun ProgressColumn() {
    CircularProgressIndicator()
}

@Composable
fun ErrorMessageColumn(item: NewArticleListItem.Error) {
    Text(item.message)
}

@Composable
fun NewArticleColumn(item: NewArticleListItem.Article) {
    var profileImage by remember(item.article.profileImageUrl) {
        mutableStateOf<ImageAsset?>(null)
    }
    rememberCoroutineScope().launch {
        profileImage = Picasso.get().getBitmapImage(item.article.profileImageUrl).asImageAsset()
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
                text = item.article.title,
                fontSize = 20.sp
            )
        }
    }
    Divider()
}

@Preview
@Composable
fun NewArticlesScreenPreview() {
    val articles = (0..30).map {
        NewArticleListItem.Article(ArticleDpo(title = "Preview Test $it"))
    }
    NewArticleList(items = articles)
}