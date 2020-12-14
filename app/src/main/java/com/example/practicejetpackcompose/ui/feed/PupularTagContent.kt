package com.example.practicejetpackcompose.ui.feed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.example.practicejetpackcompose.model.api.TagDto
import com.example.practicejetpackcompose.ui.common.FlexibleGrid
import com.example.practicejetpackcompose.ui.common.LoadingImageScreen
import com.example.practicejetpackcompose.ui.feed.preview.FeedPreviewData
import com.example.practicejetpackcompose.util.getBitmapImage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

@Composable
fun PopularTagsContent(items: FeedItem.Tags) {

    val modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp)

    Card(
        modifier = modifier,
        backgroundColor = Color.Transparent,
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(modifier = modifier) {
            FlexibleGrid { items.tags.forEach { TagContent(it) } }
        }
    }
}

@Composable
fun TagContent(
    tag: TagDto,
    onClick: (TagDto) -> Unit = {}
) {

    val imageUrl = tag.iconUrl
    var imageAsset by remember(imageUrl) { mutableStateOf<ImageAsset?>(null) }

    rememberCoroutineScope().launch {
        imageAsset = Picasso.get().getBitmapImage(imageUrl).asImageAsset()
    }

    Row(
        modifier = Modifier.wrapContentHeight().wrapContentWidth().padding(8.dp)
    ) {

        OutlinedButton(
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonConstants.defaultTextButtonColors(
                contentColor = Color.Transparent,
                backgroundColor = Color.Transparent
            ),
            modifier = Modifier.wrapContentHeight().wrapContentWidth(),
            onClick = { onClick.invoke(tag) }
        ) {

            val imageModifier = Modifier.size(12.dp).clip(RoundedCornerShape(4.dp))

            val asset = imageAsset
            if (asset != null) {
                Image(
                    modifier = imageModifier,
                    contentScale = ContentScale.FillWidth,
                    asset = asset,
                )
            } else {
                LoadingImageScreen(imageModifier)
            }
            Text(
                text = tag.id,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewPopularTagContent() {
    PopularTagsContent(
        items = FeedPreviewData.tags
            .filterIsInstance<FeedItem.Tags>()
            .first()
    )
}