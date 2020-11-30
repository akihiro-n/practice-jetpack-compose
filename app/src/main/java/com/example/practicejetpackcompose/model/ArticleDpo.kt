package com.example.practicejetpackcompose.model

data class ArticleDpo(val title: String) {

    /** 記事のレスポンスモデルから[ArticleDpo]に変換 */
    constructor(dto: ArticleDto) : this(
        title = dto.title
    )
}