package com.furkancoskun.haberler.data.repository

import com.furkancoskun.haberler.data.api.NewsService
import javax.inject.Inject

class NewsRepository
@Inject
constructor(
    private val newsService: NewsService
){
    suspend fun getNews() = newsService.getNews()
}