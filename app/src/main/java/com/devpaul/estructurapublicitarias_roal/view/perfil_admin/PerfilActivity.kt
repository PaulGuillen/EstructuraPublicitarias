package com.devpaul.estructurapublicitarias_roal.view.perfil_admin

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityPerfilBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref
import com.devpaul.estructurapublicitarias_roal.view.LoginActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import timber.log.Timber

@SuppressLint("SourceLockedOrientationActivity")
class PerfilActivity : BaseActivity() {

    lateinit var binding: ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.include.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        binding.include.toolbar.setTitleTextAppearance(this, R.style.titulosNavbar)
        binding.include.toolbar.title = "Perfil de Usuario"
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSalir.setOnClickListener { logOut() }
        getMainUserData()
    }

    private fun getMainUserData() {
        val pref = SharedPref(applicationContext)
        val name = pref.getData(NAME)
        Timber.d("PerfilActivity = $name")
    }

    private fun logOut() {
        val i = Intent(this, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        this.getSharedPreferences("Worker", 0).edit().clear().apply()
        clearPreferenceSaveUser()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearPreferenceSaveUser()
    }

    private fun clearPreferenceSaveUser() {
        val prefs = SharedPref(this)
        prefs.saveData(SaveUserInSession, INACTIVE)
        prefs.clearPreferences()
    }
}