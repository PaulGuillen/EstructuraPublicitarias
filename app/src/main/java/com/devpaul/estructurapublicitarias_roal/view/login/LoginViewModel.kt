package com.devpaul.estructurapublicitarias_roal.view.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.data.repository.LoginRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.login.LoginResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.login.LoginUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel(context: Context) : BaseViewModel() {
    private val prefs = SharedPref(context)
    val userEmail = MutableLiveData("")
    val userPassword = MutableLiveData("")

    private val _loginResult = MutableLiveData<LoginResult>()

    val loginResult: LiveData<LoginResult> = _loginResult

    val isUserSaved = MutableLiveData(false)

    init {
        Timber.d("Inicio del LoginViewModel")
    }

    fun validateUserData() {
        viewModelScope.launch {
            callServiceLogin()
        }
    }

    private suspend fun callServiceLogin() {
        val email = userEmail.value.toString()
        val password = userPassword.value.toString()
        val action = "login"

        if (!isValidForm(email = email, password = password)) {
            _loginResult.value = LoginResult.ValidationError
            _showLoadingDialog.value = false
            return
        }

        val mainUser = MainUser(
            email = email,
            password = password,
            action = action
        )
        _showLoadingDialog.value = true

        try {
            val loginRepository = LoginRepository()
            val loginUseCase = LoginUseCase(loginRepository)
            when (val requestLoginService = loginUseCase.getMainUser(mainUser)) {
                is CustomResult.OnSuccess -> {
                    val data = requestLoginService.data
                    setIsUserSaved(isUserSaved.value)
                    _loginResult.value = LoginResult.Success(data)
                }

                is CustomResult.OnError -> {
                    val error = requestLoginService.error
                    _loginResult.value = LoginResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle ?: GENERIC_ERROR_MESSAGE_LOGIN
                    )
                }
            }
        } catch (error: Exception) {
            error.message
            _loginResult.value = LoginResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }
    }

    private fun setIsUserSaved(checked: Boolean?) {
        if (checked == true) {
            prefs.saveData(SaveUserInSession, ACTIVE)
        } else {
            prefs.saveData(SaveUserInSession, INACTIVE)
        }
    }

    fun forgotPassword(){
        _loginResult.value = LoginResult.ForgotPassword
    }

}