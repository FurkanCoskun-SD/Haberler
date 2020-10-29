package com.furkancoskun.haberler.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.furkancoskun.haberler.R
import com.furkancoskun.haberler.data.model.common.AppError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException

fun Double.priceFormat(): String {
    return String.format("₺%.2f", this)
}

fun <A : Activity> Activity.startNewActivty(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun Exception.showError(context: Context?): String {
    when (this) {
        is HttpException -> {
            val response = this.response()
            val type = object : TypeToken<AppError>() {}.type
            try {
                val evderoError: AppError =
                    Gson().fromJson(response?.errorBody()?.charStream(), type)
                return evderoError.convertServerResponse(context)
            } catch (e: Exception) {
                context?.let {
                    return context.getString(R.string.ERROR)
                } ?: return "Bir hata oluştu. Lütfen tekrar deneyin"

            }
        }
        else -> return "Bir hata oluştu. Lütfen tekrar deneyin"
    }
}

fun Exception.code(): String {
    return when (this) {
        is HttpException -> {
            val response = this.response()
            val type = object : TypeToken<AppError>() {}.type
            try {
                val evderoError: AppError =
                    Gson().fromJson(response?.errorBody()?.charStream(), type)
                evderoError.code
            } catch (e: Exception) {
                "Bir hata oluştu. Lütfen tekrar deneyin"
            }
        }
        else -> "Bir hata oluştu. Lütfen tekrar deneyin"
    }
}

fun AppError.convertServerResponse(context: Context?): String {
    return try {
        context!!.getString(
            context.resources.getIdentifier(
                this.code,
                "string",
                context.packageName
            )
        )
    } catch (e: Exception) {
        this.message
    }
}

fun Fragment.safeNavigate(currentFragmentId: Int, direction: NavDirections) {
    val navController = findNavController()
    if (navController.currentDestination?.id == currentFragmentId) {
        navigate(direction)
    } else {
        Toast.makeText(context, "Navigation error", Toast.LENGTH_LONG).show()
    }
}

fun Fragment.navigate(direction: NavDirections) {
    findNavController().navigate(direction)
}

fun SpannableString.underline(start: Int, end: Int): SpannableString {
    this.setSpan(UnderlineSpan(), start, end, 0)
    return this
}

fun SpannableString.strike(start: Int, end: Int): SpannableString {
    this.setSpan(StrikethroughSpan(), start, end, 0)
    return this
}






/*
fun Fragment.navigateToWebView(title: String, url: String, shouldLoadHtml: Boolean) {
    val intent = Intent(activity, WebViewActivity::class.java)
    intent.putExtra("title", title)
    intent.putExtra("url", url)
    intent.putExtra("shouldLoadHtml", shouldLoadHtml)
    startActivity(intent)
    activity?.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
}
*/
fun RadioButton.handleSelection() {
    if (!this.isSelected) {
        this.isSelected = true
        this.isChecked = true
    } else {
        this.isSelected = false
        this.isChecked = false
    }
}

