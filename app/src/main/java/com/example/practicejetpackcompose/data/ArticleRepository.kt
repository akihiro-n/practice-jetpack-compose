package com.example.practicejetpackcompose.data

import com.example.practicejetpackcompose.model.ArticleDto
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    suspend fun getArticles(): Flow<List<ArticleDto>>
}

class ArticleRepositoryImpl(
    private val api: QiitaApi
) : ArticleRepository {

    override suspend fun getArticles() = api.getArticles()
}