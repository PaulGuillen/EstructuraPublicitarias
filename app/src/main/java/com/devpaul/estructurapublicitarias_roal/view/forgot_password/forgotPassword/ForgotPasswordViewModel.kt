package com.devpaul.estructurapublicitarias_roal.view.forgot_password.forgotPassword

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.data.repository.LoginRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword.ForgotPasswordResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword.ForgotPasswordUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(context: Context) : BaseViewModel() {

    private val prefs = SharedPref(context)

    val email = MutableLiveData("")

    private val _forgotPasswordResult = MutableLiveData<ForgotPasswordResult>()

    val forgotPasswordResult: LiveData<ForgotPasswordResult> = _forgotPasswordResult

    fun sendEmailAgain() {
        viewModelScope.launch {
            callServiceRecoveryPassword()
        }
    }

    private suspend fun callServiceRecoveryPassword() {
        val email = email.value
        val action = ACTION_RECOVER_PASSWORD

        if (!isValidFormEmail(email)) {
            _forgotPasswordResult.value = ForgotPasswordResult.NotValidForm
            _showLoadingDialog.value = false
            return
        }

        val mainUser = MainUser(
            email = email,
            action = action
        )

        _showLoadingDialog.value = true
        try {
            val loginRepository = LoginRepository()
            val loginUseCase = ForgotPasswordUseCase(loginRepository)
            when (val serviceSendEmailAgain = loginUseCase.forgotPassword(mainUser)) {
                is CustomResult.OnSuccess -> {
                    val data = serviceSendEmailAgain.data
                    prefs.saveData(KEY_PASSWORD_PREF, email.toString())
                    _forgotPasswordResult.value = ForgotPasswordResult.Success(data)
                }

                is CustomResult.OnError -> {
                    val error = serviceSendEmailAgain.error
                    _forgotPasswordResult.value = ForgotPasswordResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle ?: MESSAGE_SERVICE_NOT_AVAILABLE
                    )
                }
            }
        } catch (e: Exception) {
            _forgotPasswordResult.value = ForgotPasswordResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }
    }

}