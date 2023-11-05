package com.devpaul.estructurapublicitarias_roal.view.login

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityLoginBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.login.LoginResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.showCustomDialogErrorSingleton
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimation
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import timber.log.Timber

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
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

            is LoginResult.Error -> {
                showCustomDialogErrorSingleton(
                    this@LoginActivity,
                    result.title,
                    result.subTitle,
                    result.code,
                    "Aceptar",
                    onClickListener = {})
            }

            LoginResult.ValidationError -> {
                Timber.d("ValidationError")
            }
        }
    }


}