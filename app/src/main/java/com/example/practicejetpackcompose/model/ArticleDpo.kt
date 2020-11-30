package com.example.practicejetpackcompose.model

data class ArticleDpo(private val title: String) {

    /** 記事のレスポンスモデルから[ArticleDpo]に変換 */
    constructor(dto: ArticleDto) : this(
        title = dto.title
    )
}