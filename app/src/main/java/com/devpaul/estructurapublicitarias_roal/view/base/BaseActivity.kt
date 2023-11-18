package com.devpaul.estructurapublicitarias_roal.view.base

import android.app.Dialog
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.domain.utils.ChargeDialog
import java.io.IOException

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

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    fun getBase64ForUriAndPossiblyCrash(uri: Uri): String {
        return try {
            val bytes = contentResolver.openInputStream(uri)?.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (error: IOException) {
            error.printStackTrace()
            getString(R.string.error_base64_uri)
        }
    }
}