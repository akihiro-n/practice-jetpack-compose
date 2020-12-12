package com.example.practicejetpackcompose.ui.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicejetpackcompose.R
import com.example.practicejetpackcompose.ui.common.LoadingImageScreen
import com.example.practicejetpackcompose.util.getBitmapImage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

@Composable
fun PopularTagsContent(item: FeedItem.Tags) {

    LazyRowFor(items = item.tags) { tag ->

        val imageUrl = tag.iconUrl
        var imageAsset by remember(imageUrl) { mutableStateOf<ImageAsset?>(null) }

        rememberCoroutineScope().launch {
            imageAsset = Picasso.get().getBitmapImage(imageUrl).asImageAsset()
        }

        val rowModifier = Modifier.background(
            color = colorResource(R.color.content_dark),
            shape = RectangleShape
        ).padding(8.dp)

        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
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