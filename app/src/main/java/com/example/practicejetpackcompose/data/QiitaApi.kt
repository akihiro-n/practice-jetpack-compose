package com.example.practicejetpackcompose.data

import com.example.practicejetpackcompose.model.ArticleDto
import retrofit2.http.GET

interface QiitaApi {
    @GET("items")
    suspend fun getArticles(): List<ArticleDto>
}