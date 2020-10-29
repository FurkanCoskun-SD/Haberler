package com.furkancoskun.haberler.ui

interface DataStateChangeListener{
    fun showAlert(statusType: Int, message: String, shouldShowSuccessAlert: Boolean = false, shouldShowErrorAlert: Boolean = true)
    fun showProgressBar(display: Boolean)
    //fun showBadge(count: Int, basket: Basket? = null)
    fun hideSoftKeyboard()
    fun hideBottomBar()
    fun showBottomBar()
    fun setBottomBarBgColor(color: Int)
}