package com.devpaul.estructurapublicitarias_roal.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityMainBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimation
import com.devpaul.estructurapublicitarias_roal.view.base.WelcomeBaseActivity
import com.devpaul.estructurapublicitarias_roal.view.login.LoginActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task

class WelcomeActivity : WelcomeBaseActivity() {

    lateinit var binding: ActivityMainBinding
    private val appUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    private val updateRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ROAL)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnIngresar.setOnClickListener {
            startNewActivityWithAnimation(this@WelcomeActivity, LoginActivity::class.java, null, true)
        }
        checkForAppUpdate()
        validateUserInSessionAndThemeDark()
    }

    private fun validateUserInSession() {
        if (isUserInSession()) {
            disableLoginButton()
            showLoading()
            Handler(Looper.getMainLooper()).postDelayed({
                hideLoading()
                navigateToHome()
            }, WAIT_TIME.toLong())
        }
    }

    private fun isUserInSession(): Boolean {
        val prefs = SharedPref(this)
        return prefs.getData(SaveUserInSession) == ACTIVE
    }

    private fun disableLoginButton() {
        binding.btnIngresar.isFocusable = false
        binding.btnIngresar.isClickable = false
    }

    private fun navigateToHome() {
        startNewActivityWithAnimation(this@WelcomeActivity, HomeActivity::class.java, null, true)
    }

    private fun validateUserInSessionAndThemeDark() {
        val isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        if (isDarkMode) {
            showConfirmationThemeDarkDialog()
        } else {
            validateUserInSession()
        }
    }

    private fun checkForAppUpdate() {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    updateRequestCode
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == updateRequestCode) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Debe actualizar", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
