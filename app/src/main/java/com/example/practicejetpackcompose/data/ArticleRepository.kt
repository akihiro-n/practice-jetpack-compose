package com.example.practicejetpackcompose.data

import com.example.practicejetpackcompose.model.ArticleDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface ArticleRepository {
    fun getArticles(): Flow<List<ArticleDto>>
}

class ArticleRepositoryImpl(
    private val api: QiitaApi
) : ArticleRepository {

    override fun getArticles() =
        flow { emit(api.getArticles()) }.flowOn(Dispatchers.IO)
}