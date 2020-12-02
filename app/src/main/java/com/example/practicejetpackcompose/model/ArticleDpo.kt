package com.example.practicejetpackcompose.model

import com.example.practicejetpackcompose.model.api.ArticleDto

data class ArticleDpo(
    val title: String,
    val profileImageUrl: String? = null
) {

    /** 記事のレスポンスモデルから[ArticleDpo]に変換 */
    constructor(dto: ArticleDto) : this(
        title = dto.title,
        profileImageUrl = dto.user.profileImageUrl
    )
}