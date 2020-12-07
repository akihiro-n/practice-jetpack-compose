package com.example.practicejetpackcompose.model.api

import com.squareup.moshi.Json

data class ArticleDto(
    val user: UserDto,
    val title: String,
    val tags: List<ArticleTagDto>,
    @Json(name = "rendered_body") val renderedBody: String?,
    @Json(name = "updated_at") val updatedAt: String?,
    @Json(name = "likes_count") val likesCount: String?,
    @Json(name = "page_views_count") val pageViewsCount: String?,
)