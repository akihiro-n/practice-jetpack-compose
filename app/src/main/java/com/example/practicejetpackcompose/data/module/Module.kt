package com.example.practicejetpackcompose.data.module

import com.example.practicejetpackcompose.data.ArticleRepository
import com.example.practicejetpackcompose.data.ArticleRepositoryImpl
import com.example.practicejetpackcompose.data.QiitaApi
import com.example.practicejetpackcompose.data.TagRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object Module {

    @Provides
    @Singleton
    fun articleRepository(qiitaApi: QiitaApi): ArticleRepository =
        ArticleRepositoryImpl(api = qiitaApi)

    @Provides
    @Singleton
    fun tagRepository(qiitaApi: QiitaApi): TagRepositoryImpl =
        TagRepositoryImpl(api = qiitaApi)
}