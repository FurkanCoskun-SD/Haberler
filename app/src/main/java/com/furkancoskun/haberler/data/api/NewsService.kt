package com.furkancoskun.haberler.data.api

import com.furkancoskun.haberler.data.model.response.NewsResponse
import retrofit2.http.GET

interface NewsService {
    @GET("services/haberlercom/2.11/service.asmx/haberler?category=manset&count=35&offset=0&deviceType=1&userId=61ed99e0c09a8664")
    suspend fun getNews(): NewsResponse
}