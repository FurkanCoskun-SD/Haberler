package com.furkancoskun.haberler.di.network.baseURL

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object BaseURLModule {
    @BaseURL
    @Singleton
    @Provides
    fun provideBaseURL(): String = "https://app.haberler.com/"
}