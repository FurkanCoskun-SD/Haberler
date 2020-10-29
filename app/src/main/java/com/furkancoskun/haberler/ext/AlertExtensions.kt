package com.furkancoskun.haberler.ext

import android.app.Activity
import com.furkancoskun.haberler.R
import com.furkancoskun.haberler.data.model.common.AlertType


fun Activity.displayDialog(message: String?, alertType: AlertType) {


}

interface AlertCallback {
    fun proceed()
    fun cancel()
}

fun Activity.displayDialogWithCallback(errorMessage: String?, callback: AlertCallback, statusType: Int, btnTitle: String? = getString(
    R.string.OK), shouldShowCancel: Boolean? = false) {


}

