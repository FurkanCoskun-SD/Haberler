package com.furkancoskun.haberler.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.furkancoskun.haberler.data.repository.NewsRepository
import com.furkancoskun.haberler.utils.Resource
import kotlinx.coroutines.Dispatchers

class NewsViewModel @ViewModelInject constructor(
    private val newsRepository: NewsRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getNews() = liveData(
        Dispatchers.IO
    ) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = newsRepository.getNews()))
        } catch (e: KotlinNullPointerException) {
            emit(Resource.empty(data = null, exception = e))
        } catch (e: Exception) {
            emit(Resource.error(data = null, exception = e))
        }
    }
}