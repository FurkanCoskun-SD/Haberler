package com.furkancoskun.haberler.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.furkancoskun.haberler.R
import retrofit2.HttpException
import java.lang.Exception

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    EMPTY
}

data class Resource<out T>(val status: Status, val data: T?, val exception: Exception?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = Status.SUCCESS, data = data, exception = null)

        fun <T> error(data: T?, exception: Exception): Resource<T> =
            Resource(status = Status.ERROR, data = data, exception = exception)

        fun <T> empty(data: T?, exception: Exception?): Resource<T> =
            Resource(status = Status.EMPTY, data = null, exception = null)

        fun <T> loading(data: T?): Resource<T> = Resource(status = Status.LOADING, data = data, exception = null)
    }
}

open class ResponseHandler {
    fun <T : Any> handleSuccess(data: T): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        return when (e) {
            is HttpException -> Resource.error(data = null, exception = e)
            is KotlinNullPointerException -> Resource.empty(data = null, exception = null)
            else -> Resource.error(data = null, exception = e)
        }
    }
}

/*object CommonUtils {
    fun showLoadingDialog(context: Context): Dialog {
        val progressDialog = Dialog(context)

        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setContentView(R.layout.layout_loading)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)


            return it
        }
    }
}*/