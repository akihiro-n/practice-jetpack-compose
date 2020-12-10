package com.example.practicejetpackcompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.example.practicejetpackcompose.R
import com.example.practicejetpackcompose.model.ArticleDpo
import com.example.practicejetpackcompose.model.api.ArticleDto
import com.example.practicejetpackcompose.model.api.ArticleTagDto
import com.example.practicejetpackcompose.model.api.TagDto
import com.example.practicejetpackcompose.model.api.UserDto
import com.example.practicejetpackcompose.ui.common.ErrorMessageScreen
import com.example.practicejetpackcompose.ui.common.LoadingImageScreen
import com.example.practicejetpackcompose.ui.common.ProgressScreen
import com.example.practicejetpackcompose.util.getBitmapImage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

private const val POSITION_NEXT_PAGE_LOADING = 3

@FlowPreview
@Composable
fun NewArticlesScreen() {
    val viewModel = viewModel<NewArticleViewModel>()
    remember { viewModel.fetchFeed() }
    NewArticleList(
        items = viewModel.items,
        onStartLoadNextPage = { viewModel.fetchNextArticles() }
    )
}

@Composable
fun NewArticleList(
    items: List<NewArticleListItem>,
    onStartLoadNextPage: () -> Unit
) {

    LazyColumnForIndexed(items = items) { position, item ->

        when (item) {
            is NewArticleListItem.Progress -> ProgressScreen()
            is NewArticleListItem.Error -> ErrorMessageScreen(item.message)
            is NewArticleListItem.Tags -> PopularTagsColumn(item)
            is NewArticleListItem.Article -> {

                /**
                 * 次のページを読み込む
                 */
                if (position == (items.size - POSITION_NEXT_PAGE_LOADING)) {
                    onActive { onStartLoadNextPage.invoke() }
                }

                NewArticleColumn(item)
            }
            is NewArticleListItem.Divider -> ColumnSpaceDivider()
        }
    }
}

@Composable
fun PopularTagsColumn(item: NewArticleListItem.Tags) {

    LazyRowFor(items = item.tags) { tag ->

        val imageUrl = tag.iconUrl
        var imageAsset by remember(imageUrl) { mutableStateOf<ImageAsset?>(null) }

        rememberCoroutineScope().launch {
            imageAsset = Picasso.get().getBitmapImage(imageUrl).asImageAsset()
        }

        val imageModifier = Modifier.size(12.dp).clip(RoundedCornerShape(4.dp))
        val asset = imageAsset

        Row(
            modifier = Modifier.background(
                color = colorResource(R.color.content_dark),
                shape = RectangleShape
            ).padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (asset != null) {
                Image(
                    modifier = imageModifier,
                    contentScale = ContentScale.FillWidth,
                    asset = asset,
                )
            } else {
                LoadingImageScreen(modifier = imageModifier)
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = tag.id,
                color = Color.White,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier.width(8.dp))
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

                    val imageModifier = Modifier.size(24.dp).clip(CircleShape)
                    val asset = profileImage

                    if (asset != null) {
                        Image(
                            modifier = imageModifier,
                            contentScale = ContentScale.FillWidth,
                            asset = asset,
                        )
                    } else {
                        LoadingImageScreen(modifier = imageModifier)
                    }
                }
            }

            val imageModifier = Modifier.fillMaxWidth().height(128.dp)
            val asset = profileImage

            if (asset != null) {
                Image(
                    modifier = imageModifier,
                    contentScale = ContentScale.FillWidth,
                    asset = asset,
                )
            } else {
                LoadingImageScreen(modifier = imageModifier)
            }
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
                Text(
                    text = stringResource(R.string.likes_icon),
                    color = Color.Red,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.article.likesCount, color = Color.Gray, fontSize = 12.sp)

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.views_icon),
                    color = Color.Yellow,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.article.pageViewsCount, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ColumnSpaceDivider() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview
@Composable
fun PreviewNewArticlesScreen() {

    // Preview表示用データ
    val tags = listOf(
        NewArticleListItem.Tags(tags = (0..10).map {
            TagDto(
                id = "$it Kotlin",
                followersCount = "${it * 10}",
                itemsCount = "${it * 20}",
                iconUrl = null
            )
        }),
        NewArticleListItem.Divider
    )

    val articles = (0..30).map {
        listOf(
            NewArticleListItem.Article(
                ArticleDpo(
                    ArticleDto(
                        user = UserDto(
                            name = "user name",
                            description = null,
                            profileImageUrl = null
                        ),
                        title = "article title",
                        tags = listOf(ArticleTagDto(name = "Kotlin")),
                        renderedBody = "Preview Content",
                        updatedAt = "0000 0000",
                        likesCount = "${it * 2}",
                        pageViewsCount = "$it"
                    )
                )
            ),
            NewArticleListItem.Divider
        )
    }.flatten()

    NewArticleList(
        items = tags + articles,
        onStartLoadNextPage = {}
    )
}