package com.devpaul.estructurapublicitarias_roal.view.base

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.devpaul.estructurapublicitarias_roal.R
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

    fun showConfirmationThemeDarkDialog() {
        val dialog = AlertDialog.Builder(this).create()
        val dialogView = layoutInflater.inflate(R.layout.custom_theme_dialog_dark, null)
        dialog.setView(dialogView)

        val messageTextView = dialogView.findViewById<TextView>(R.id.textMessage)
        val buttonCloseDialog = dialogView.findViewById<Button>(R.id.btnCloseDialogThemeDark)

        messageTextView.text = getString(R.string.dark_mode_message)

        if (dialog.window != null) {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        buttonCloseDialog.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            recreate()
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

}