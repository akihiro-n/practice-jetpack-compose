package com.example.practicejetpackcompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
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
import com.example.practicejetpackcompose.R
import com.example.practicejetpackcompose.ui.common.ErrorMessageScreen
import com.example.practicejetpackcompose.ui.common.LoadingImageScreen
import com.example.practicejetpackcompose.ui.common.ProgressScreen
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
    LazyColumnFor(items = items) { item ->
        when (item) {
            is NewArticleListItem.Progress -> ProgressScreen()
            is NewArticleListItem.Error -> ErrorMessageScreen(item.message)
            is NewArticleListItem.Article -> NewArticleColumn(item)
            is NewArticleListItem.Divider -> ColumnSpaceDivider()
        }
    }
}

@Composable
fun NewArticleColumn(item: NewArticleListItem.Article) {

    val imageUrl = item.article.profileImageUrl
    var profileImage by remember(imageUrl) { mutableStateOf<ImageAsset?>(null) }
    rememberCoroutineScope().launch {
        profileImage = Picasso.get().getBitmapImage(imageUrl).asImageAsset()
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .heightIn(200.dp)
            .fillMaxWidth(),
        backgroundColor = colorResource(R.color.content_dark)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = item.article.updatedAt,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
                Row(
                    modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.article.userName,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.width(16.dp))

                    val imageModifier = Modifier.size(36.dp).clip(CircleShape)
                    profileImage?.let { asset ->
                        Image(
                            modifier = imageModifier,
                            contentScale = ContentScale.FillWidth,
                            asset = asset,
                        )
                    } ?: LoadingImageScreen(modifier = imageModifier)
                }
            }

            val imageModifier = Modifier.fillMaxWidth().height(128.dp)
            profileImage?.let { asset ->
                Image(
                    modifier = imageModifier,
                    contentScale = ContentScale.FillWidth,
                    asset = asset,
                )
            } ?: LoadingImageScreen(modifier = imageModifier)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.article.title,
                    maxLines = 2,
                    color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = item.article.plainTextBody,
                    maxLines = 2,
                    color = Color.LightGray,
                    fontSize = 16.sp
                )
            }
            Divider(color = Color.Black)
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.article.likesCount, color = Color.Gray, fontSize = 12.sp)
                Text(text = item.article.pageViewsCount, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ColumnSpaceDivider() {
    Spacer(modifier = Modifier.height(8.dp))
}

//@Preview
//@Composable
//fun NewArticlesScreenPreview() {
//     Preview表示用データ
//    val articles = (0..30).map {
//        NewArticleListItem.Article(ArticleDpo(title = "Preview Test $it"))
//    }
//    NewArticleList(items = articles)
//}