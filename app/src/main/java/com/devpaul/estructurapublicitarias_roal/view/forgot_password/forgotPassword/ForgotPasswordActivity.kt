package com.devpaul.estructurapublicitarias_roal.view.forgot_password.forgotPassword

import android.os.Bundle
import android.text.TextUtils
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.LoginUseCase
import com.devpaul.estructurapublicitarias_roal.data.repository.LoginRepository
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityForgotPasswordBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.domain.utils.showCustomDialogErrorSingleton
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimation
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithBackAnimation
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.devpaul.estructurapublicitarias_roal.view.forgot_password.codeVerification.CodeVerificationActivity
import com.devpaul.estructurapublicitarias_roal.view.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ForgotPasswordActivity : BaseActivity() {
    lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRecuperarContrasena.setOnClickListener { recoveryPassword() }
        binding.btnBack.setOnClickListener { backView() }
    }

    private fun recoveryPassword() {
        val email = binding.textEmail.text.toString()
        val action = "recuperar_contraseña"

        if (isValidForm(email)) {
            val mainUser = MainUser(
                email = email,
                action = action
            )
            showLoading()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val loginRepository = LoginRepository()
                    val loginUseCase = LoginUseCase(this@ForgotPasswordActivity, loginRepository)
                    val responseChangePassword = loginUseCase.forgotPassword(mainUser)
                    withContext(Dispatchers.Main) {
                        hideLoading()
                        when (responseChangePassword) {
                            is CustomResult.OnSuccess -> {
                                val data = responseChangePassword.data
                                goToCodeVerification()
                            }

                            is CustomResult.OnError -> {
                                val codeState = SingletonError.code
                                val title = SingletonError.title
                                val subTitle = SingletonError.subTitle
                                showCustomDialogErrorSingleton(this@ForgotPasswordActivity, title, subTitle, codeState, "Aceptar", onClickListener = {})
                            }
                        }
                    }

                } catch (e: Exception) {
                    Timber.d("Response $e")
                }
            }
        }
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidForm(email: String): Boolean {
        if (email.isBlank()) {
            return false
        }
        if (!email.isEmailValid()) {
            return false
        }
        return true
    }

    private fun goToCodeVerification() {
        val prefs = SharedPref(this)
        val email = binding.textEmail.text.toString()

        if (!email.isNullOrBlank()) {
            prefs.saveData("EmailForgotPassword", email)
        }

        startNewActivityWithAnimation(this@ForgotPasswordActivity, CodeVerificationActivity::class.java)
    }

    private fun backView() {
        startNewActivityWithBackAnimation(this@ForgotPasswordActivity, LoginActivity::class.java)
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        backView()
    }
}