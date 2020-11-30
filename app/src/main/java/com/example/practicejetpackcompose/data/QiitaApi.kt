package com.example.practicejetpackcompose.data

import com.example.practicejetpackcompose.model.ArticleDto
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface QiitaApi {
    @GET("items")
    suspend fun getArticles(): Flow<List<ArticleDto>>
}