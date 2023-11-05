package com.devpaul.estructurapublicitarias_roal.view.forgot_password.newPassword

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.data.repository.LoginRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword.ForgotPasswordResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword.ForgotPasswordUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.domain.utils.isValidFormNewPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPasswordViewModel(context: Context) : ViewModel() {

    private val prefs = SharedPref(context)

    val newPassword = MutableLiveData("")
    val confirmNewPassword = MutableLiveData("")

    private val _newPasswordResult = MutableLiveData<ForgotPasswordResult>()

    val newPasswordResult: LiveData<ForgotPasswordResult> = _newPasswordResult

    private val _showLoadingDialog = MutableLiveData<Boolean>()
    val showLoadingDialog: LiveData<Boolean>
        get() = _showLoadingDialog

    fun validateNewPassword() {
        viewModelScope.launch {
            callServiceNewPassword()
        }
    }

    private suspend fun callServiceNewPassword() {

        val newPassword = newPassword.value.toString()
        val confirmNewPassword = confirmNewPassword.value.toString()
        val action = "change_password"
        val email = prefs.getData("EmailForgotPassword")

        if (!isValidFormNewPassword(newPassword, confirmNewPassword)) {
            _newPasswordResult.value = ForgotPasswordResult.NotValidForm
            _showLoadingDialog.value = false
            return
        }

        val mainUser = MainUser(
            new_password = newPassword,
            email = email,
            action = action
        )

        _showLoadingDialog.value = true

        try {
            val loginRepository = LoginRepository()
            val loginUseCase = ForgotPasswordUseCase(loginRepository)
            val responseChangePassword = loginUseCase.changePassword(mainUser)
            withContext(Dispatchers.Main) {

                when (responseChangePassword) {
                    is CustomResult.OnSuccess -> {
                        val data = responseChangePassword.data
                        _newPasswordResult.value = ForgotPasswordResult.Success(data)
                    }

                    is CustomResult.OnError -> {
                        val error = responseChangePassword.error
                        _newPasswordResult.value = ForgotPasswordResult.Error(
                            error.code ?: SingletonError.code,
                            error.title ?: SingletonError.title,
                            error.subtitle
                        )
                    }
                }
            }

        } catch (e: Exception) {
            _newPasswordResult.value = ForgotPasswordResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }

    }

}