package com.devpaul.estructurapublicitarias_roal.view

import android.os.Bundle
import android.widget.Toast
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.LoginUseCase
import com.devpaul.estructurapublicitarias_roal.data.repository.LoginRepository
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityNewPasswordBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.domain.utils.showCustomDialog
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimation
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithBackAnimation
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class NewPasswordActivity : BaseActivity() {

    lateinit var binding: ActivityNewPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnActualizar.setOnClickListener { sendingNewPassword() }
        binding.btnBack.setOnClickListener { backView() }
    }

    private fun sendingNewPassword() {

        val newPassword = binding.textPassword.text.toString()
        val confirmNewPassword = binding.textConfirmPassword.text.toString()
        val action = "change_password"
        val email = intent.getStringExtra("email")
        if (isValidForm(newPassword, confirmNewPassword)) {
            val mainUser = MainUser(
                new_password = newPassword,
                email = email,
                action = action
            )
            showLoading()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val loginRepository = LoginRepository()
                    val loginUseCase = LoginUseCase(this@NewPasswordActivity, loginRepository)
                    val responseChangePassword = loginUseCase.changePassword(mainUser)
                    withContext(Dispatchers.Main) {
                        hideLoading()
                        when (responseChangePassword) {
                            is CustomResult.OnSuccess -> {
                                val data = responseChangePassword.data
                                goToHomeDashboard()
                            }

                            is CustomResult.OnError -> {
                                val codeState = SingletonError.code
                                val title = SingletonError.title
                                val subTitle = SingletonError.subTitle
                                showCustomDialog(this@NewPasswordActivity, title, subTitle, codeState, "Aceptar", onClickListener = {})
                            }
                        }
                    }

                } catch (e: Exception) {
                    Timber.d("Response $e")
                }
            }
        }
    }

    private fun isValidForm(
        password: String,
        confirmPassword: String
    ): Boolean {

        if (password.isBlank()) {
            Toast.makeText(this, "Debes ingresar la contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        if (confirmPassword.isBlank()) {
            Toast.makeText(this, "Debes ingresar el la confirmación de contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun goToHomeDashboard() {
        startNewActivityWithAnimation(this@NewPasswordActivity, LoginActivity::class.java, null, true)
    }

    private fun backView() {
        startNewActivityWithBackAnimation(this@NewPasswordActivity, ForgotPasswordActivity::class.java)
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        backView()
    }

}