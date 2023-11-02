package com.devpaul.estructurapublicitarias_roal.view.base

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.domain.utils.ChargeDialog
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref

abstract class BaseActivity : InitialActivity() {

    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateThemeColorDark()
    }

    private fun validateThemeColorDark() {
        val isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        if (isDarkMode) {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.dark_mode_message))
        builder.setPositiveButton(getString(R.string.message_ok_welcome)) { _, _ ->
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            recreate()
        }
        builder.setCancelable(false)
        builder.show()
    }

    fun hideLoading() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    fun showLoading() {
        progressDialog = ChargeDialog.showLoadingDialog(this)
    }

}