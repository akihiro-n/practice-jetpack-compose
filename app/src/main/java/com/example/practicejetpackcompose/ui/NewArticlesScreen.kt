package com.example.practicejetpackcompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.example.practicejetpackcompose.R
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = colorResource(R.color.background_dark))
    ) {
        LazyColumnFor(items = items) { item ->
            when (item) {
                is NewArticleListItem.Progress -> ProgressColumn()
                is NewArticleListItem.Error -> ErrorMessageColumn(item)
                is NewArticleListItem.Article -> NewArticleColumn(item)
                is NewArticleListItem.Space -> Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ProgressColumn() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessageColumn(item: NewArticleListItem.Error) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(item.message)
    }
}

@Composable
fun NewArticleColumn(item: NewArticleListItem.Article) {

    val imageUrl = item.article.profileImageUrl
    var profileImage by remember(imageUrl) {
        mutableStateOf<ImageAsset?>(null)
    }
    rememberCoroutineScope().launch {
        profileImage = Picasso.get().getBitmapImage(imageUrl).asImageAsset()
    }

    Card(
        elevation = 2.dp,
        modifier = Modifier.padding(horizontal = 16.dp).heightIn(200.dp).fillMaxWidth(),
        backgroundColor = colorResource(R.color.content_dark),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            profileImage?.let { asset ->
                Image(
                    modifier = Modifier.fillMaxWidth().height(128.dp),
                    contentScale = ContentScale.FillWidth,
                    asset = asset
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                profileImage?.let { asset ->
                    Image(
                        modifier = Modifier.width(48.dp).height(48.dp).clip(CircleShape),
                        contentScale = ContentScale.FillWidth,
                        asset = asset
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = item.article.title,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun NewArticlesScreenPreview() {
    // Preview表示用データ
    val articles = (0..30).map {
        NewArticleListItem.Article(ArticleDpo(title = "Preview Test $it"))
    }
    NewArticleList(items = articles)
}