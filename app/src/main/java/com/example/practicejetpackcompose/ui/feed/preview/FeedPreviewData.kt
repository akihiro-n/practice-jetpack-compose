package com.example.practicejetpackcompose.ui.feed.preview

import com.example.practicejetpackcompose.model.ArticleDpo
import com.example.practicejetpackcompose.model.api.ArticleDto
import com.example.practicejetpackcompose.model.api.ArticleTagDto
import com.example.practicejetpackcompose.model.api.TagDto
import com.example.practicejetpackcompose.model.api.UserDto
import com.example.practicejetpackcompose.ui.feed.FeedItem

/**
 * Feed画面のPreview表示用データ
 */
object FeedPreviewData {

    val tags = listOf(
        FeedItem.Tags(tags = (0..10).map {
            TagDto(
                id = "$it Kotlin",
                followersCount = "${it * 10}",
                itemsCount = "${it * 20}",
                iconUrl = null
            )
        }),
        FeedItem.Divider
    )

    val articles = (0..30).map {
        listOf(
            FeedItem.Article(
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
            FeedItem.Divider
        )
    }.flatten()
}
