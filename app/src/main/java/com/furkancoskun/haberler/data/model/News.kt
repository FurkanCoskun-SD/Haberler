package com.furkancoskun.haberler.data.model

data class News(
    val category: String,
    val title: String,
    val spot: String,
    val redirects: Boolean,
    val isAdvertorial: Boolean,
    val publishDate: String,
    val id: Int,
    val imageUrl: String,
    val videoUrl: String,
    val webUrl: String,
    val commentCount: Int,
    val imageSize: String,
    val body: List<NewsBody>
)