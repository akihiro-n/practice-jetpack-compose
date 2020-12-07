package com.example.practicejetpackcompose.domain

import com.example.practicejetpackcompose.model.ArticleDpo
import com.example.practicejetpackcompose.model.api.ArticleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Flow<List<ArticleDto>>.toDpo(): Flow<List<ArticleDpo>> =
    map { it.map { dto -> ArticleDpo(dto = dto) } }
