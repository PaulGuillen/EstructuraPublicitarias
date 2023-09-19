package com.devpaul.estructurapublicitarias_roal.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityMainBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.ACTIVE
import com.devpaul.estructurapublicitarias_roal.domain.utils.SaveUserInSession
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimationAndClearTask
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity

class WelcomeActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ROAL)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnIngresar.setOnClickListener {
            startNewActivityWithAnimationAndClearTask(this@WelcomeActivity, LoginActivity::class.java)
        }
        validateUserInSession()
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
                startNewActivityWithAnimationAndClearTask(this@WelcomeActivity, HomeActivity::class.java)
            }, 5000)
        }
    }
}
