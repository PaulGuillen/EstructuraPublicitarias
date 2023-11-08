package com.devpaul.estructurapublicitarias_roal.view.forgot_password.codeVerification

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.data.repository.LoginRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword.ForgotPasswordResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword.ForgotPasswordUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CodeVerificationViewModel(context: Context) : ViewModel() {

    private val prefs = SharedPref(context)

    val firstInput = MutableLiveData("")
    val secondInput = MutableLiveData("")
    val thirdInput = MutableLiveData("")
    val fourthInput = MutableLiveData("")
    val fiveInput = MutableLiveData("")

    val textTimer = MutableLiveData<String>()

    private var countDownTimer: CountDownTimer? = null
    private var millisUntilFinished: Long = 0

    private val _codeVerificationResult = MutableLiveData<ForgotPasswordResult>()

    val codeVerificationResult: LiveData<ForgotPasswordResult> = _codeVerificationResult

    private val _showLoadingDialog = MutableLiveData<Boolean>()
    val showLoadingDialog: LiveData<Boolean>
        get() = _showLoadingDialog


    private val _showReSendButton = MutableLiveData<Boolean>()
    val showReSendButton: LiveData<Boolean> = _showReSendButton

    private val _showRecoverPasswordButton = MutableLiveData<Boolean>()
    val showRecoverPasswordButton: LiveData<Boolean> = _showRecoverPasswordButton


    init {
        startTimer()
    }

    fun validateCodeVerification() {
        viewModelScope.launch {
            callServiceCodeVerification()
        }
    }

    fun sendEmailAgain() {
        viewModelScope.launch {
            callServiceSendEmailAgain()
        }
    }

    private suspend fun callServiceCodeVerification() {

        val email = prefs.getData("EmailForgotPassword")

        val numberOne = firstInput.value.toString()
        val numberTwo = secondInput.value.toString()
        val numberThree = thirdInput.value.toString()
        val numberFour = fourthInput.value.toString()
        val numberFive = fiveInput.value.toString()
        val fullNumber = "$numberOne$numberTwo$numberThree$numberFour$numberFive"
        val action = "code_verification"

        if (!isValidFormCodeVerification(numberOne, numberTwo, numberThree, numberFour, numberFive)) {
            _codeVerificationResult.value = ForgotPasswordResult.NotValidForm
            _showLoadingDialog.value = false
            return
        }

        val mainUser = MainUser(
            code = fullNumber.toInt(),
            email = email,
            action = action
        )

        _showLoadingDialog.value = true

        try {
            val loginRepository = LoginRepository()
            val loginUseCase = ForgotPasswordUseCase(loginRepository)
            val requestLoginService = loginUseCase.codeVerification(mainUser)
            withContext(Dispatchers.Main) {
                when (requestLoginService) {
                    is CustomResult.OnSuccess -> {
                        val data = requestLoginService.data
                        _codeVerificationResult.value = ForgotPasswordResult.Success(data)
                    }

                    is CustomResult.OnError -> {
                        val error = requestLoginService.error
                        _codeVerificationResult.value = ForgotPasswordResult.Error(
                            error.code ?: SingletonError.code,
                            error.title ?: SingletonError.title,
                            error.subtitle ?: MESSAGE_SERVICE_NOT_AVAILABLE
                        )
                    }
                }
            }

        } catch (e: Exception) {
            _codeVerificationResult.value = ForgotPasswordResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }
    }

    private suspend fun callServiceSendEmailAgain() {
        val email = prefs.getData(KEY_PASSWORD_PREF)
        val action = ACTION_RECOVER_PASSWORD

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
                    withContext(Dispatchers.Main) {
                        startTimer()
                    }
                    _codeVerificationResult.value = ForgotPasswordResult.ReSendEmailSuccess
                }

                is CustomResult.OnError -> {
                    val error = serviceSendEmailAgain.error
                    _codeVerificationResult.value = ForgotPasswordResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle
                    )
                }
            }
        } catch (e: Exception) {
            _codeVerificationResult.value = ForgotPasswordResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }
    }


    private fun startTimer() {
        millisUntilFinished = TIME_LEFT_FORGOT_PASSWORD.toLong()
        textTimer.value = formatTime(millisUntilFinished)
        updateButtonVisibility()
        countDownTimer = object : CountDownTimer(millisUntilFinished, 1000) {
            override fun onTick(time: Long) {
                textTimer.value = formatTime(time)
            }

            override fun onFinish() {
                millisUntilFinished = 0
                updateButtonVisibility()
            }
        }
        countDownTimer?.start()
    }

    private fun formatTime(time: Long): String {
        return "Tiempo restante: ${(time / 1000)}s"
    }

    override fun onCleared() {
        countDownTimer?.cancel()
        super.onCleared()
    }

    fun updateButtonVisibility() {
        val currentTime = millisUntilFinished
        val showReSend = currentTime <= 0
        val showRecoverPassword = currentTime > 0

        _showReSendButton.value = showReSend
        _showRecoverPasswordButton.value = showRecoverPassword
    }

}