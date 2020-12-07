package com.example.practicejetpackcompose.data.module

import com.example.practicejetpackcompose.data.*
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
    fun tagRepository(qiitaApi: QiitaApi): TagRepository =
        TagRepositoryImpl(api = qiitaApi)
}