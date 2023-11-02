package com.devpaul.estructurapublicitarias_roal.view.base

import android.app.Dialog
import android.os.Bundle
import com.devpaul.estructurapublicitarias_roal.domain.utils.ChargeDialog

abstract class WelcomeBaseActivity : InitialActivity() {

    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

     fun hideLoading() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    fun showLoading() {
        progressDialog = ChargeDialog.showLoadingDialog(this)
    }
}