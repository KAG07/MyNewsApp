package com.example.mynews.ui.models

import com.example.mynews.ui.models.Article

data class NewsResponse(
    var articles: List<Article>,
    var status: String,
    var totalResults: Int
)