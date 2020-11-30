package com.example.practicejetpackcompose

interface Constants {
    val baseUrl: String
}

class ConstantsImpl : Constants {

    override val baseUrl: String
        get() = "https://qiita.com/api/v2/"
}