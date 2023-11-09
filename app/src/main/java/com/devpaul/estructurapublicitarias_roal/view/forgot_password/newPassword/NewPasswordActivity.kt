package com.devpaul.estructurapublicitarias_roal.view.forgot_password.newPassword

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityNewPasswordBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword.ForgotPasswordResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.forgot_password.forgotPassword.ForgotPasswordActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.devpaul.estructurapublicitarias_roal.view.login.LoginActivity

class NewPasswordActivity : BaseActivity() {

    lateinit var binding: ActivityNewPasswordBinding
    private lateinit var viewModel: NewPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(this, NewPasswordViewModel::class.java))[NewPasswordViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnActualizar.setOnClickListener {
            viewModel.validateNewPassword()
        }
        viewModel.newPasswordResult.observe(this) { result ->
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
                startNewActivityWithAnimation(this, LoginActivity::class.java, null, true)
                showToast(SUCCESS_MESSAGE_CHANGE_PASSWORD)
            }

            is ForgotPasswordResult.Error -> {
                showCustomDialogErrorSingleton(this,
                    result.title,
                    result.subTitle,
                    result.code,
                    "Aceptar",
                    onClickListener = {})
            }

            is ForgotPasswordResult.NotValidForm -> {
                showToast(MESSAGE_DATA_NOT_VALID)
            }

            is ForgotPasswordResult.ValidationError -> {

            }

            else -> {}
        }
    }

    private fun backView() {
        startNewActivityWithBackAnimation(this@NewPasswordActivity, ForgotPasswordActivity::class.java)
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        backView()
    }

}