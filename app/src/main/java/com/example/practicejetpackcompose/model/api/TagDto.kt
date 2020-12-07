package com.example.practicejetpackcompose.model.api

import com.squareup.moshi.Json

data class TagDto(
    val id: String,
    @Json(name = "followers_count") val followersCount: String?,
    @Json(name = "items_count") val itemsCount: String?,
    @Json(name = "icon_url") val iconUrl: String?
)