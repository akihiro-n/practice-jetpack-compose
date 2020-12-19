package com.example.practicejetpackcompose.ui.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.example.practicejetpackcompose.R
import com.example.practicejetpackcompose.ui.common.LoadingImageScreen
import com.example.practicejetpackcompose.ui.feed.preview.FeedPreviewData
import com.example.practicejetpackcompose.util.asImageAssetAsync
import com.example.practicejetpackcompose.util.getBitmapImage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

@Composable
fun ArticleContent(
    item: FeedItem.Article,
    onClick: (FeedItem.Article) -> Unit = {}
) {

    val imageUrl = item.article.profileImageUrl
    var profileImage by remember(imageUrl) { mutableStateOf<ImageAsset?>(null) }
    rememberCoroutineScope().launch {
        profileImage = Picasso.get().getBitmapImage(imageUrl).asImageAssetAsync()
    }

    val contentModifier = Modifier
        .clickable(onClick = { onClick.invoke(item) })
        .padding(horizontal = 8.dp)
        .heightIn(200.dp)
        .fillMaxWidth()

    Card(
        modifier = contentModifier,
        backgroundColor = colorResource(R.color.content_dark)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
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
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
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
                    maxLines = 4,
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
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.article.likesCount, color = Color.Gray, fontSize = 12.sp)

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.views_icon),
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.article.pageViewsCount, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Preview
@Composable
fun PreviewArticleContent() {
    ArticleContent(
        item = FeedPreviewData.articles
            .filterIsInstance<FeedItem.Article>()
            .first()
    )
}