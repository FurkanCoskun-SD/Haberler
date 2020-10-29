package com.furkancoskun.haberler.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.furkancoskun.haberler.R
import com.furkancoskun.haberler.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun displayProgressBar(display: Boolean) {
        if(display) {
            showLoading()
        } else { hideLoading()}
    }

    private fun showLoading() {
        hideLoading()
        //loadingDialog = CommonUtils.showLoadingDialog(this)
    }

    private fun hideLoading() {
        loadingDialog?.let { if(it.isShowing)it.cancel() }
    }

    override fun hideBottomBar() {

    }

    override fun showBottomBar() {

    }
}