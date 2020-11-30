package com.example.practicejetpackcompose.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewArticlesActivity : AppCompatActivity() {

    private val viewModel by viewModels<NewArticleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewArticlesScreen()
        }
        viewModel.fetchNewArticles()
    }
}