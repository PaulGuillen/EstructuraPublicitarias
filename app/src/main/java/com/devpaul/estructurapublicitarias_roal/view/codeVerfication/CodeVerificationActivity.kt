package com.devpaul.estructurapublicitarias_roal.view.codeVerfication

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityCodeVerificationBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword.ForgotPasswordResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.ViewModelFactory
import com.devpaul.estructurapublicitarias_roal.domain.utils.showCustomDialogErrorSingleton
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimation
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithBackAnimation
import com.devpaul.estructurapublicitarias_roal.view.ForgotPasswordActivity
import com.devpaul.estructurapublicitarias_roal.view.NewPasswordActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity

class CodeVerificationActivity : BaseActivity() {

    lateinit var binding: ActivityCodeVerificationBinding
    private lateinit var viewModel: CodeVerificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(this, CodeVerificationViewModel::class.java))[CodeVerificationViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnRecuperarContrasena.setOnClickListener {
            viewModel.validateCodeVerification()
        }

        binding.btnReenviarCorreo.setOnClickListener {
            viewModel.sendEmailAgain()
        }
        binding.btnBack.setOnClickListener {
            backView()
        }

        viewModel.codeVerificationResult.observe(this) { result ->
            handleForgotPasswordResult(result)
        }

        viewModel.showLoadingDialog.observe(this) { showLoading ->
            if (showLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun handleForgotPasswordResult(result: ForgotPasswordResult) {
        when (result) {
            is ForgotPasswordResult.Success -> {
                startNewActivityWithAnimation(this, NewPasswordActivity::class.java, null, true)
            }

            is ForgotPasswordResult.Error -> {
                showCustomDialogError(result)
                setButtonVisibility(showReSend = true, showRecoverPassword = false)
            }

            is ForgotPasswordResult.ReSendEmailSuccess -> {
                setButtonVisibility(showReSend = false, showRecoverPassword = true)
            }

            ForgotPasswordResult.ValidationError -> {
                setButtonVisibility(showReSend = true, showRecoverPassword = false)
            }
        }
    }

    private fun showCustomDialogError(result: ForgotPasswordResult.Error) {
        showCustomDialogErrorSingleton(
            this,
            result.title,
            result.subTitle,
            result.code,
            "Aceptar",
            onClickListener = {}
        )
    }

    private fun setButtonVisibility(showReSend: Boolean, showRecoverPassword: Boolean) {
        binding.btnReenviarCorreo.visibility = if (showReSend) View.VISIBLE else View.GONE
        binding.btnRecuperarContrasena.visibility = if (showRecoverPassword) View.VISIBLE else View.GONE
    }

    private fun backView() {
        startNewActivityWithBackAnimation(this@CodeVerificationActivity, ForgotPasswordActivity::class.java)
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        backView()
    }
}