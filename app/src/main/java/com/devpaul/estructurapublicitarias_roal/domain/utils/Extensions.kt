package com.devpaul.estructurapublicitarias_roal.domain.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.devpaul.estructurapublicitarias_roal.R
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import kotlin.math.roundToInt

fun toolbarStyle(context: Context, toolbar: Toolbar, title: String, activeToolbar: Boolean? = null, targetActivity: Class<*>? = null) {
    targetActivity?.let { configureToolbar(context as AppCompatActivity, toolbar, title, activeToolbar, it) }
    toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white))
    toolbar.setTitleTextAppearance(context, R.style.titulosNavbar)
}

fun configureToolbar(activity: AppCompatActivity, toolbar: Toolbar, title: String, activeToolbar: Boolean?, targetActivity: Class<*>) {
    activity.setSupportActionBar(toolbar)
    val actionBar = activity.supportActionBar
    actionBar?.title = title
    if (activeToolbar == false) {
        actionBar?.setDisplayHomeAsUpEnabled(false)
    } else {
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            startNewActivityWithBackAnimation(activity, targetActivity)
        }
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

fun showCustomDialogErrorSingleton(
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

fun startNewActivityWithAnimation(context: Context, targetActivity: Class<*>, extras: Bundle? = null, clearTask: Boolean = false) {
    val intent = Intent(context, targetActivity)
    extras?.let {
        intent.putExtras(it)
    }
    val options = ActivityOptionsCompat.makeCustomAnimation(context, R.transition.slide_in, R.transition.slide_out)

    if (clearTask) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent, options.toBundle())
}

fun startNewActivityWithBackAnimation(context: Context, targetActivity: Class<*>) {
    val intent = Intent(context, targetActivity)
    val options = ActivityOptionsCompat.makeCustomAnimation(context, R.transition.slide_out_reverse, R.transition.slide_in_reverse)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent, options.toBundle())
}

fun setSVGColorFromResource(imageButton: ImageButton, colorResource: Int) {
    val color = ContextCompat.getColor(imageButton.context, colorResource)
    val originalDrawable = imageButton.drawable
    val clonedDrawable = originalDrawable.constantState?.newDrawable()

    if (clonedDrawable != null) {
        DrawableCompat.setTint(clonedDrawable, color)
        imageButton.setImageDrawable(clonedDrawable)
    }
}

fun showImageValidateEPP(context: Context) {
    val customDialogView = LayoutInflater.from(context).inflate(R.layout.item_image_validate_epp, null)

    val dialog = AlertDialog.Builder(context)
        .setView(customDialogView)
        .setCancelable(true)
        .create()

    if (dialog.window != null) {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    dialog.show()
}

fun showDialogDetailsValidateEquipment(context: Context, details: String?, area: String?) {
    val customDialogView = LayoutInflater.from(context).inflate(R.layout.cardview_details_validate_epp, null)

    val dialog = AlertDialog.Builder(context)
        .setView(customDialogView)
        .setCancelable(true)
        .create()

    if (dialog.window != null) {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    val areaTextView = customDialogView.findViewById<TextView>(R.id.area)
    val detailsTextView = customDialogView.findViewById<TextView>(R.id.details)
    val buttonCloseDialog = customDialogView.findViewById<ImageButton>(R.id.btnCloseDialog)

    val formattedDetails = formatDetailsText(details)

    areaTextView.text = area
    detailsTextView.text = formattedDetails

    buttonCloseDialog.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

private fun formatDetailsText(details: String?): String {
    val lines = details?.split("\n")
    return lines?.joinToString("\n") ?: ""
}


fun applyCustomTextStyleToTextView(textView: TextView, titleText: String?) {
    textView.text = titleText
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
    textView.typeface = ResourcesCompat.getFont(textView.context, R.font.mulish_bold)
    textView.setTextColor(ContextCompat.getColor(textView.context, android.R.color.holo_blue_light))
    textView.setTypeface(textView.typeface, Typeface.BOLD)
    textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun isValidFormEmail(email: String?): Boolean {
    if (email?.isBlank() == true) {
        return false
    }
    if (!email?.isEmailValid()!!) {
        return false
    }
    return true
}

fun isValidForm(email: String, password: String): Boolean {

    if (email.isBlank()) {
        return false
    }
    if (password.isBlank()) {
        return false
    }
    if (!email.isEmailValid()) {
        return false
    }
    return true
}

fun isValidFormCodeVerification(
    numberOne: String,
    numberTwo: String,
    numberThree: String,
    numberFour: String,
    fullNumber: String
): Boolean {
    if (numberOne.isBlank()) {
        return false
    }
    if (numberTwo.isBlank()) {
        return false
    }
    if (numberThree.isBlank()) {
        return false
    }
    if (numberFour.isBlank()) {
        return false
    }
    if (fullNumber.isBlank()) {
        return false
    }
    return true
}

fun isValidFormNewPassword(password: String, confirmPassword: String): Boolean {
    if (password.isBlank()) {
        return false
    }
    if (confirmPassword.isBlank()) {
        return false
    }
    if (password != confirmPassword) {
        return false
    }
    return true
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun isValidFormUpdateWorker(
    area: String,
    phone: String,
    phoneEmergency: String
): Boolean {

    if (area.isBlank()) {
        return false
    }

    if (phone.isBlank() || phone.length < 9) {
        return false
    }

    if (phoneEmergency.isBlank()) {
        return false
    }

    return true
}

fun toggleTextInputLayoutError(
    textInputEditText: TextInputEditText,
    msg: String?
) {
    textInputEditText.error = msg
    textInputEditText.isTextInputLayoutFocusedRectEnabled = msg != null
}


fun <T : CardView?> applyButtonSelectionLogic(
    buttons: List<T>,
    selectedColor: Int,
    unselectedColor: Int,
    onClick: (T) -> Unit
) {
    var selectedButton: T? = null
    buttons.forEach { button ->
        button?.setOnClickListener {
            selectedButton?.setBackgroundColor(unselectedColor)
            button.setBackgroundColor(selectedColor)
            selectedButton = button
            onClick(button)
        }
    }
}

class RoundedPercentageFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value.roundToInt()}%"
    }
}
