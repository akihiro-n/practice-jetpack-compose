package com.example.practicejetpackcompose.di

import com.example.practicejetpackcompose.BuildConfig
import com.example.practicejetpackcompose.Constants
import com.example.practicejetpackcompose.ConstantsImpl
import com.example.practicejetpackcompose.data.ArticleRepository
import com.example.practicejetpackcompose.data.ArticleRepositoryImpl
import com.example.practicejetpackcompose.data.QiitaApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideMoshiConvertorFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder().also {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }
        }.build()

    @Provides
    fun provideQiitaApi(
        httpClient: OkHttpClient,
        constants: Constants,
        moshiConverterFactory: MoshiConverterFactory
    ): QiitaApi = Retrofit.Builder()
        .client(httpClient)
        .addConverterFactory(moshiConverterFactory)
        .baseUrl(constants.baseUrl)
        .build()
        .create(QiitaApi::class.java)

    @Provides
    @Singleton
    fun provideConstants(): Constants = ConstantsImpl()
}