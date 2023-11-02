package com.devpaul.estructurapublicitarias_roal.view

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityMainBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimation
import com.devpaul.estructurapublicitarias_roal.view.base.WelcomeBaseActivity

class WelcomeActivity : WelcomeBaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ROAL)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnIngresar.setOnClickListener {
            startNewActivityWithAnimation(this@WelcomeActivity, LoginActivity::class.java, null, true)
        }
        validateUserInSessionAndThemeDark()
    }

    private fun validateUserInSession() {
        val prefs = SharedPref(this)
        val savedValue = prefs.getData(SaveUserInSession)
        if (savedValue == ACTIVE) {
            showLoading()
            binding.btnIngresar.isFocusable = false
            binding.btnIngresar.isClickable = false
            Handler(Looper.getMainLooper()).postDelayed({
                hideLoading()
                startNewActivityWithAnimation(this@WelcomeActivity, HomeActivity::class.java, null, true)
            }, 5000)
        }
    }

    private fun validateUserInSessionAndThemeDark() {
        val isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        if (isDarkMode) {
            showConfirmationDialog()
        } else {
            validateUserInSession()
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Actualmente la aplicación no soporta el modo oscuro. Solo se desactivara en la aplicación actual")
        builder.setPositiveButton("Sí") { _, _ ->
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            recreate()
        }
        builder.setCancelable(false)
        builder.show()
    }

}
