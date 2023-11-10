package com.devpaul.estructurapublicitarias_roal.view.login

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityLoginBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.login.LoginResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.MESSAGE_DATA_NOT_VALID
import com.devpaul.estructurapublicitarias_roal.domain.utils.ViewModelFactory
import com.devpaul.estructurapublicitarias_roal.domain.utils.showCustomDialogErrorSingleton
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimation
import com.devpaul.estructurapublicitarias_roal.view.forgot_password.forgotPassword.ForgotPasswordActivity
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelFactory(this, LoginViewModel::class.java))[LoginViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnLogin.setOnClickListener {
            viewModel.validateUserData()
        }

        viewModel.loginResult.observe(this) { result ->
            handleLoginResult(result)
        }

        viewModel.showLoadingDialog.observe(this) { showLoading ->
            if (showLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun handleLoginResult(result: LoginResult) {
        when (result) {
            is LoginResult.Success -> {
                startNewActivityWithAnimation(this, HomeActivity::class.java, null, true)
            }

            is LoginResult.ForgotPassword -> {
                startNewActivityWithAnimation(this@LoginActivity, ForgotPasswordActivity::class.java, null)
            }

            is LoginResult.Error -> {
                showCustomDialogErrorSingleton(this,
                    result.title,
                    result.subTitle,
                    result.code,
                    "Aceptar",
                    onClickListener = {})
            }

            is LoginResult.ValidationError -> {
                Toast.makeText(this, result.errorMessage ?: MESSAGE_DATA_NOT_VALID, Toast.LENGTH_SHORT).show()
            }
        }
    }


}