package com.devpaul.estructurapublicitarias_roal.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.LoginUseCase
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.data.repository.LoginRepository
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityCodeVerificationBinding
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

class CodeVerificationActivity : BaseActivity() {

    lateinit var binding: ActivityCodeVerificationBinding
    var countDownTimer: CountDownTimer? = null
    var millisUntilFinished: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRecuperarContrasena.setOnClickListener { goToNewPassword() }
        binding.btnReenviarCorreo.setOnClickListener { sendingAgainEmail() }
        binding.btnBack.setOnClickListener { backView() }
        timerToShowView()
    }

    private fun sendingAgainEmail() {
        val email = intent.getStringExtra("email")
        val action = "recuperar_contraseña"
        val mainUser = MainUser(
            email = email,
            action = action
        )
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val loginRepository = LoginRepository()
                val loginUseCase = LoginUseCase(this@CodeVerificationActivity, loginRepository)
                val responseForgotPassword = loginUseCase.forgotPassword(mainUser)
                withContext(Dispatchers.Main) {
                    hideLoading()
                    when (responseForgotPassword) {
                        is CustomResult.OnSuccess -> {
                            val data = responseForgotPassword.data
                            binding.btnReenviarCorreo.visibility = View.GONE
                            binding.btnRecuperarContrasena.visibility = View.VISIBLE
                            timerToShowView()
                        }

                        is CustomResult.OnError -> {
                            val codeState = SingletonError.code
                            val title = SingletonError.title
                            val subTitle = SingletonError.subTitle
                            showCustomDialog(this@CodeVerificationActivity, title, subTitle, codeState, "Aceptar", onClickListener = {})
                        }
                    }
                }

            } catch (e: Exception) {
                Timber.d("Response $e")
            }
        }
    }

    private fun goToNewPassword() {
        val numberOne = binding.textNumberOne.text.toString()
        val numberTwo = binding.textNumberTwo.text.toString()
        val numberThree = binding.textNumberThree.text.toString()
        val numberFour = binding.textNumberFour.text.toString()
        val numberFive = binding.textNumberFive.text.toString()
        val fullNumber = "$numberOne$numberTwo$numberThree$numberFour$numberFive"
        val action = "code_verification"
        val email = intent.getStringExtra("email")

        if (isValidForm(numberOne, numberTwo, numberThree, numberFour, numberFive)) {
            val mainUser = MainUser(
                code = fullNumber.toInt(),
                email = email,
                action = action
            )
            showLoading()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val loginRepository = LoginRepository()
                    val loginUseCase = LoginUseCase(this@CodeVerificationActivity, loginRepository)
                    val requestLoginService = loginUseCase.codeVerification(mainUser)
                    withContext(Dispatchers.Main) {
                        hideLoading()
                        when (requestLoginService) {
                            is CustomResult.OnSuccess -> {
                                val datas = requestLoginService.data
                                goToNewPasswordView()
                            }

                            is CustomResult.OnError -> {
                                val codeState = SingletonError.code
                                val title = SingletonError.title
                                val subTitle = SingletonError.subTitle
                                showCustomDialog(this@CodeVerificationActivity, title, subTitle, codeState, "Aceptar", onClickListener = {})
                            }
                        }
                    }

                } catch (e: Exception) {
                    Timber.d("Response $e")
                }
            }

        } else {
            Toast.makeText(this@CodeVerificationActivity, "Datos no válidos", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun timerToShowView() {
        millisUntilFinished = 60000
        binding.textTimer.text = "" + (millisUntilFinished?.div(1000)) + "s"
        val countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(time: Long) {
                binding.textTimer.text = "Tiempo restante : " + (time / 1000) + "s"
            }

            override fun onFinish() {
                binding.btnReenviarCorreo.visibility = View.VISIBLE
                binding.btnRecuperarContrasena.visibility = View.GONE
            }
        }
        countDownTimer.start()
    }

    private fun isValidForm(
        numberOne: String,
        numberTwo: String,
        numberThree: String,
        numberFour: String,
        fullNumber: String
    ): Boolean {
        if (numberOne.isBlank()) {
            return false
        }
        if (numberTwo.isBlank()) {
            return false
        }
        if (numberThree.isBlank()) {
            return false
        }
        if (numberFour.isBlank()) {
            return false
        }
        if (fullNumber.isBlank()) {
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        countDownTimer?.cancel()
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.cancel()
    }

    private fun goToNewPasswordView() {
        val extras = Bundle()
        val email = intent.getStringExtra("email")
        extras.putString("email", email)
        startNewActivityWithAnimation(this@CodeVerificationActivity, NewPasswordActivity::class.java, extras, true)
    }

    private fun backView() {
        countDownTimer?.cancel()
        startNewActivityWithBackAnimation(this@CodeVerificationActivity, ForgotPasswordActivity::class.java)
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        backView()
    }
}