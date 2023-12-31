package com.devpaul.estructurapublicitarias_roal.view.base

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.WelcomeActivity
import timber.log.Timber

abstract class InitialActivity : AppCompatActivity() {

    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var timeOutApp = TIME_OUT_DEFAULT
    private var scan = true
    private var onScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blockOrientationApp()
        initTimer()
        startHandler()
        TimberFactory.setupOnDebug()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun blockOrientationApp(){
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun initTimer() {
        try {
            handler = Handler(Looper.getMainLooper())
            runnable = Runnable {
                if (!isFinishing) {
                    scan = false
                    showCustomLogoutView()
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex, "Something happened when trying to show `EndSession` dialog")
        }
    }

    private fun startHandler() {
        handler?.postDelayed(runnable!!, timeOutApp.toLong())
    }

    private fun showCustomLogoutView() {
        if (!scan && onScreen) {
            val customLogoutView = layoutInflater.inflate(R.layout.dialog_end_session, null)
            val rootView = findViewById<ViewGroup>(android.R.id.content)
            rootView.addView(customLogoutView)

            val customLogoutButton = customLogoutView.findViewById<Button>(R.id.dialogButton)
            customLogoutButton.setOnClickListener {
                clearPreferences()
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        stopHandler()
        if (scan) {
            startHandler()
        }
    }

    private fun stopHandler() {
        handler?.removeCallbacks(runnable!!)
    }


    override fun onPause() {
        super.onPause()
        onScreen = false
    }

    override fun onResume() {
        super.onResume()
        onScreen = true
    }

    fun clearPreferences() {
        val prefs = SharedPref(this)
        prefs.clearPreferences()
    }

}