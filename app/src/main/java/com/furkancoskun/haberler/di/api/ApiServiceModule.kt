package com.furkancoskun.haberler.di.api

import com.furkancoskun.haberler.data.api.NewsService
import com.furkancoskun.haberler.di.network.AppServiceRetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApiServiceModule {

    @Singleton
    @Provides
    fun provideNewsService(@AppServiceRetrofitInstance retrofit: Retrofit): NewsService {
        return retrofit.create(NewsService::class.java)
    }

}