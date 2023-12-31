package com.devpaul.estructurapublicitarias_roal.view.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import com.devpaul.estructurapublicitarias_roal.BuildConfig
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivitySettingsBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithBackAnimation
import com.devpaul.estructurapublicitarias_roal.domain.utils.toolbarStyle
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity
import com.devpaul.estructurapublicitarias_roal.view.login.LoginActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity

@SuppressLint("SourceLockedOrientationActivity")
class SettingsActivity : BaseActivity() {

    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarStyle(this@SettingsActivity, binding.include.toolbar, "Ajustes", true, HomeActivity::class.java)
        actionsInView()
    }

    private fun actionsInView() {
        binding.clinearlayoutShare.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "ROAL esta disponible en: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)

        }

        binding.clinearlayoutTerms.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data =
                Uri.parse("https://paulguillen.github.io/Terminos-Condiciones/")
            startActivity(openURL)
        }

        binding.clinearlayoutPolicy.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data =
                Uri.parse("https://paulguillen.github.io/Politicas-Privacidad/")
            startActivity(openURL)
        }

        val versionName = BuildConfig.VERSION_NAME
        val textFullVersion = getString(R.string.app_versi_n) + "\r" + versionName
        binding.version.text = textFullVersion

        binding.btnSalir.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            clearPreferences()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startNewActivityWithBackAnimation(this@SettingsActivity, HomeActivity::class.java)
    }
}