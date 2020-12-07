package com.example.practicejetpackcompose.data

import com.example.practicejetpackcompose.model.api.ArticleDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface ArticleRepository {

    companion object {
        private const val PER_PAGE = 30L
    }

    fun getArticles(
        page: Long,
        perPage: Long = PER_PAGE,
        query: String? = null
    ): Flow<List<ArticleDto>>
}

class ArticleRepositoryImpl(
    private val api: QiitaApi
) : ArticleRepository {

    override fun getArticles(page: Long, perPage: Long, query: String?) =
        flow {
            val articles = api.getArticles(
                page = page.toString(),
                perPage = perPage.toString(),
                query = query
            )
            emit(articles)
        }.flowOn(Dispatchers.IO)
}