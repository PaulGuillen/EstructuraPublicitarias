package com.devpaul.estructurapublicitarias_roal.view.forgot_password.forgotPassword

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityForgotPasswordBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword.ForgotPasswordResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.devpaul.estructurapublicitarias_roal.view.forgot_password.codeVerification.CodeVerificationActivity
import com.devpaul.estructurapublicitarias_roal.view.login.LoginActivity

class ForgotPasswordActivity : BaseActivity() {

    lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(this, ForgotPasswordViewModel::class.java))[ForgotPasswordViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnRecuperarContrasena.setOnClickListener {
            viewModel.sendEmailAgain()
        }

        viewModel.forgotPasswordResult.observe(this) { result ->
            handleForgotPasswordResult(result)
        }

        viewModel.showLoadingDialog.observe(this) { showLoading ->
            if (showLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        binding.btnBack.setOnClickListener { backView() }
    }

    private fun handleForgotPasswordResult(result: ForgotPasswordResult) {
        when (result) {
            is ForgotPasswordResult.Success -> {
                startNewActivityWithAnimation(this, CodeVerificationActivity::class.java, null, true)
            }

            is ForgotPasswordResult.Error -> {
                showCustomDialogError(result)
            }

            is ForgotPasswordResult.NotValidForm -> {
                Toast.makeText(this, MESSAGE_DATA_NOT_VALID, Toast.LENGTH_SHORT).show()
            }

            is ForgotPasswordResult.ValidationError -> {
                Toast.makeText(this, GENERIC_SERVICE_ERROR, Toast.LENGTH_SHORT).show()
            }

            else -> {
                /**No contemplado*/
            }
        }
    }

    fun showCustomDialogError(result: ForgotPasswordResult.Error) {
        showCustomDialogErrorSingleton(
            this,
            result.title,
            result.subTitle,
            result.code,
            getString(R.string.dialog_singleton_text_button_accept),
            onClickListener = {}
        )
    }

    private fun backView() {
        startNewActivityWithBackAnimation(this@ForgotPasswordActivity, LoginActivity::class.java)
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        backView()
    }
}