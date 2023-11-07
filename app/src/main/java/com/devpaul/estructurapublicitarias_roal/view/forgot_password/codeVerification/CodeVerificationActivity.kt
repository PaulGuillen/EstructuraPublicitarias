package com.devpaul.estructurapublicitarias_roal.view.forgot_password.codeVerification

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityCodeVerificationBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword.ForgotPasswordResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.forgot_password.forgotPassword.ForgotPasswordActivity
import com.devpaul.estructurapublicitarias_roal.view.forgot_password.newPassword.NewPasswordActivity
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

        viewModel.showReSendButton.observe(this) { showReSend ->
            binding.btnReenviarCorreo.visibility = if (showReSend) View.VISIBLE else View.GONE
        }

        viewModel.showRecoverPasswordButton.observe(this) { showRecoverPassword ->
            binding.btnRecuperarContrasena.visibility = if (showRecoverPassword) View.VISIBLE else View.GONE
        }

    }

    private fun handleForgotPasswordResult(result: ForgotPasswordResult) {
        when (result) {
            is ForgotPasswordResult.Success -> {
                startNewActivityWithAnimation(this, NewPasswordActivity::class.java, null, true)
            }

            is ForgotPasswordResult.Error -> {
                showCustomDialogError(result)
                setButtonVisibility(showReSendEmail = true, showRecoverPassword = false)
            }

            is ForgotPasswordResult.ReSendEmailSuccess -> {
                setButtonVisibility(showReSendEmail = false, showRecoverPassword = true)
            }

            is ForgotPasswordResult.NotValidForm -> {
                Toast.makeText(this, MESSAGE_DATA_NOT_VALID, Toast.LENGTH_SHORT).show()
            }

            is ForgotPasswordResult.ValidationError -> {
                setButtonVisibility(showReSendEmail = true, showRecoverPassword = false)
            }

        }
    }

    private fun setButtonVisibility(showReSendEmail: Boolean, showRecoverPassword: Boolean) {
        binding.btnReenviarCorreo.visibility = if (showReSendEmail) View.VISIBLE else View.GONE
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