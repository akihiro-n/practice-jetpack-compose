package com.example.practicejetpackcompose.model

data class ArticleDpo(
    val title: String,
    val profileImageUrl: String? = null
) {

    /** 記事のレスポンスモデルから[ArticleDpo]に変換 */
    constructor(dto: ArticleDto) : this(
        title = dto.title,
        profileImageUrl = dto.profileImageUrl
    )
}