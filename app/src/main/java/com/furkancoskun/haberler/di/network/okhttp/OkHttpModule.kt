package com.furkancoskun.haberler.di.network.okhttp

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object OkHttpModule {

    @Provides
    fun getInterceptor(apiKeyHeader: String): Interceptor {
        return Interceptor { chain ->
            val newUrl = chain.request().url
                .newBuilder()
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apiKeyHeader)
                .build()

            chain.proceed(newRequest)
        }
    }

    @AppServiceInterceptorOkHttpClient
    @Singleton
    @Provides
    fun provideAppServiceInterceptorOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}