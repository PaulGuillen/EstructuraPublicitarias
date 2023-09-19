package com.devpaul.estructurapublicitarias_roal.domain.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.devpaul.estructurapublicitarias_roal.R
import java.io.File


fun toolbarStyle(context: Context, toolbar: Toolbar, title: String, activeToolbar: Boolean? = null) {
    configureToolbar(context as AppCompatActivity, toolbar, title, activeToolbar)
    toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white))
    toolbar.setTitleTextAppearance(context, R.style.titulosNavbar)
}

fun configureToolbar(activity: AppCompatActivity, toolbar: Toolbar, title: String, activeToolbar: Boolean?) {
    activity.setSupportActionBar(toolbar)
    val actionBar = activity.supportActionBar
    if (activeToolbar == false){
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }else{
        actionBar?.title = title
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
    }

}

fun deleteCache(context: Context) {
    try {
        val dir: File = context.cacheDir
        deleteDir(dir)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun deleteDir(dir: File?): Boolean {
    return if (dir != null && dir.isDirectory) {
        val children: Array<String> = dir.list() as Array<String>
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
        dir.delete()
    } else if (dir != null && dir.isFile) {
        dir.delete()
    } else {
        false
    }
}

fun showErrorAlert(context: Context, title: String, subtitle: String) {
    SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        .setTitleText(title)
        .setContentText(subtitle)
        .show()
}

fun isValidPhoneNumber(regexPhone: Regex, phone: String): Boolean {

    if (!phone.matches(regexPhone)) {
        return false
    }
    if (!phone.validateNumberRepeatDigits()) {
        return false
    }

    return true
}

fun String.validateNumberRepeatDigits(): Boolean {
    return this.trim() != "999999999"
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun showCustomDialog(
    context: Context,
    title: String? = null,
    message: String? = null,
    code: Int? = null,
    buttonText: String? = null,
    onClickListener: (() -> Unit)? = null
) {
    val customDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_login_error, null)

    val dialog = AlertDialog.Builder(context)
        .setView(customDialogView)
        .setCancelable(false)
        .create()

    val dialogTitle = customDialogView.findViewById<TextView>(R.id.titleService)
    val dialogMessage = customDialogView.findViewById<TextView>(R.id.textMessageError)
    val dialogButton = customDialogView.findViewById<Button>(R.id.btnAccept)

    if (dialog.window != null) {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    val messageToShow = "$message - $code"

    dialogTitle.text = title
    dialogMessage.text = messageToShow

    if (onClickListener != null) {
        dialogButton.text = buttonText
        dialogButton.setOnClickListener {
            onClickListener.invoke()
            dialog.dismiss()
        }
    } else {
        dialogButton.visibility = View.GONE
    }

    dialog.show()
}

fun startNewActivityWithAnimation(context: Context, targetActivity: Class<*>, extras: Bundle? = null) {
    val intent = Intent(context, targetActivity)
    extras?.let {
        intent.putExtras(it)
    }
    val options = ActivityOptionsCompat.makeCustomAnimation(context, R.transition.slide_in, R.transition.slide_out)
    context.startActivity(intent, options.toBundle())
}

fun startNewActivityWithAnimationAndClearTask(context: Context, targetActivity: Class<*>, extras: Bundle? = null) {
    val intent = Intent(context, targetActivity)
    extras?.let {
        intent.putExtras(it)
    }
    val options = ActivityOptionsCompat.makeCustomAnimation(context, R.transition.slide_in, R.transition.slide_out)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent, options.toBundle())
}

fun startNewActivityWithBackAnimation(context: Context, targetActivity: Class<*>) {
    val intent = Intent(context, targetActivity)
    val options = ActivityOptionsCompat.makeCustomAnimation(context, R.transition.slide_out_reverse, R.transition.slide_in_reverse)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent, options.toBundle())
}