package com.example.practicejetpackcompose.model

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.example.practicejetpackcompose.model.api.ArticleDto

data class ArticleDpo(val dto: ArticleDto) {

    val title: String
        get() = dto.title

    val profileImageUrl: String?
        get() = dto.user.profileImageUrl

    /**
     * [ArticleDto.renderedBody]がHTML形式なので
     * タグを除去した文字列に変換して返す
     */
    val plainTextBody: String
        get() = HtmlCompat.fromHtml(
            dto.renderedBody.orEmpty(),
            FROM_HTML_MODE_COMPACT
        ).toString()

    val userName: String
        get() = dto.user.name

    val likesCount: String
        get() = dto.likesCount ?: "0"

    val pageViewsCount: String
        get() = dto.pageViewsCount ?: "0"

    // TODO: Format
    val updatedAt: String
        get() = dto.updatedAt.orEmpty()
}