package com.example.practicejetpackcompose.data

import com.example.practicejetpackcompose.model.ArticleDto

interface ArticleRepository {
    suspend fun getArticles(): List<ArticleDto>
}


class ArticleRepositoryImpl(
    private val api: QiitaApi
) : ArticleRepository {

    override suspend fun getArticles() = api.getArticles()
}