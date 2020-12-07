package com.example.practicejetpackcompose.data

import com.example.practicejetpackcompose.model.api.ArticleDto
import com.example.practicejetpackcompose.model.api.TagDto
import retrofit2.http.GET
import retrofit2.http.Query

interface QiitaApi {

    @GET("items")
    suspend fun getArticles(
        @Query("page") page: String,
        @Query("per_page") perPage: String,
        @Query("query") query: String?
    ): List<ArticleDto>

    @GET("tags")
    suspend fun getTags(
        @Query("page") page: String,
        @Query("per_page") perPage: String,
        @Query("sort") sort: String
    ): List<TagDto>
}
