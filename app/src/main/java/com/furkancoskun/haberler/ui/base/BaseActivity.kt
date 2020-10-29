package com.furkancoskun.haberler.ui.base

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.furkancoskun.haberler.data.model.common.AlertType
import com.furkancoskun.haberler.ext.displayDialog
import com.furkancoskun.haberler.ext.displayDialogWithCallback
import com.furkancoskun.haberler.ui.DataStateChangeListener
import com.furkancoskun.haberler.ui.UICommunicationListener
import com.furkancoskun.haberler.ui.UIMessage
import com.furkancoskun.haberler.ui.UIMessageType

abstract class BaseActivity : AppCompatActivity(), DataStateChangeListener,
    UICommunicationListener {

    override fun showAlert(
        statusType: Int,
        message: String,
        shouldShowSuccessAlert: Boolean,
        shouldShowErrorAlert: Boolean
    ) {
        when (statusType) {
            AlertType.SUCCESS.code ->
                if(shouldShowSuccessAlert) displayDialog(message, AlertType.SUCCESS)
            AlertType.INFO.code -> if (shouldShowErrorAlert) displayDialog(message, AlertType.INFO)

            else -> if (shouldShowErrorAlert) displayDialog(message, AlertType.ERROR)
        }
    }

    override fun onUIMessageReceived(uiMessage: UIMessage, statusType: Int, btnTitle: String?, shouldShowCancel: Boolean?) {
        when(uiMessage.uiMessageType){
            is UIMessageType.Dialog -> {
                displayDialogWithCallback(
                    uiMessage.message,
                    uiMessage.uiMessageType.callback,
                    statusType,
                    btnTitle,
                    shouldShowCancel
                )
            }
            is UIMessageType.None -> {
                Log.d( "BaseActivity", ", onUIMessageReceived : ${uiMessage.message}")
            }
        }
    }

    // Progress Bar
    override fun showProgressBar(display: Boolean) {
        displayProgressBar(display)
    }

    abstract fun displayProgressBar(display: Boolean)

    // Hide Keyboard
    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun setBottomBarBgColor(color: Int) {}
}