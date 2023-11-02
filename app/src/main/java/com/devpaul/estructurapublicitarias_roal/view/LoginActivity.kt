package com.devpaul.estructurapublicitarias_roal.view

import android.os.Bundle
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.data.repository.LoginRepository
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityLoginBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.ACTIVE
import com.devpaul.estructurapublicitarias_roal.domain.utils.INACTIVE
import com.devpaul.estructurapublicitarias_roal.domain.utils.SaveUserInSession
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.LoginUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.domain.utils.isEmailValid
import com.devpaul.estructurapublicitarias_roal.domain.utils.showCustomDialogErrorSingleton
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimation
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding
    private var saveUserInSession: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener { goToHomeView() }
        binding.textForgotPassword.setOnClickListener { gotToForgotPasswordView() }
    }

    private fun goToHomeView() {

        val email = binding.textEmail.text.toString()
        val password = binding.textPassword.text.toString()
        val action = "login"

        if (isValidForm(email = email, password = password)) {
            val mainUser = MainUser(
                email = email,
                password = password,
                action = action
            )
            showLoading()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val loginRepository = LoginRepository()
                    val loginUseCase = LoginUseCase(this@LoginActivity, loginRepository)
                    val requestLoginService = loginUseCase.getMainUser(mainUser)
                    withContext(Dispatchers.Main) {
                        hideLoading()
                        when (requestLoginService) {
                            is CustomResult.OnSuccess -> {
                                val data = requestLoginService.data
                                validateUserInSession()
                                goToHomeDashboard()
                            }

                            is CustomResult.OnError -> {
                                val codeState = SingletonError.code
                                val titleState = SingletonError.title
                                val subTitleState = if (SingletonError.subTitle.isNullOrEmpty()) {
                                    "Clave y/o usuarios incorrectos"
                                } else {
                                    SingletonError.subTitle
                                }

                                showCustomDialogErrorSingleton(this@LoginActivity, titleState, subTitleState, codeState, "Aceptar", onClickListener = {})
                            }
                        }
                    }

                } catch (e: Exception) {
                    Timber.d("Response $e")
                }
            }
        }
    }


    private fun validateUserInSession() {
        val prefs = SharedPref(this@LoginActivity)
        saveUserInSession = binding.saveStateUser.isChecked
        if (saveUserInSession) {
            prefs.saveData(SaveUserInSession, ACTIVE)
        } else {
            prefs.saveData(SaveUserInSession, INACTIVE)
        }
    }

    private fun isValidForm(email: String, password: String): Boolean {

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

    private fun goToHomeDashboard() {
        startNewActivityWithAnimation(this@LoginActivity, HomeActivity::class.java,null,true)
    }

    private fun gotToForgotPasswordView() {
        startNewActivityWithAnimation(this@LoginActivity, ForgotPasswordActivity::class.java,null,true)
    }
}