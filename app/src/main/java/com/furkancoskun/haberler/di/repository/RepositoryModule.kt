package com.furkancoskun.haberler.di.repository

import com.furkancoskun.haberler.data.api.NewsService
import com.furkancoskun.haberler.data.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(userService: NewsService): NewsRepository {
        return NewsRepository(userService)
    }

}