package com.devpaul.estructurapublicitarias_roal.domain.usecases.login

import com.devpaul.estructurapublicitarias_roal.data.models.entity.GeneralHTTP

sealed class LoginResult {
    data class Success(val data: GeneralHTTP) : LoginResult()
    data class Error(val code: Int?, val title: String?, val subTitle: String?) : LoginResult()
    data object ForgotPassword : LoginResult()
    data class ValidationError(val errorMessage: String?) : LoginResult()
}
