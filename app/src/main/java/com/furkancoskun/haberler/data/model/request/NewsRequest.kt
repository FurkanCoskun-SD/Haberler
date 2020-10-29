package com.furkancoskun.haberler.data.model.request

data class NewsRequest(
    val category: String,
    val count: String,
    val offset: String,
    val deviceType: String,
    val userId: String,
)