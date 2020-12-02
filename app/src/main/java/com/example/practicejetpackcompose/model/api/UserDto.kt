package com.example.practicejetpackcompose.model.api

import com.squareup.moshi.Json

data class UserDto(
    val description: String?,
    @Json(name = "profile_image_url")
    val profileImageUrl: String?
)