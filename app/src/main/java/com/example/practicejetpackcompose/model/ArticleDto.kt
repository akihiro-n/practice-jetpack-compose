package com.example.practicejetpackcompose.model

import com.squareup.moshi.Json

data class ArticleDto(
    val title: String,
    @Json(name = "profile_image_url")
    val profileImageUrl: String?
)