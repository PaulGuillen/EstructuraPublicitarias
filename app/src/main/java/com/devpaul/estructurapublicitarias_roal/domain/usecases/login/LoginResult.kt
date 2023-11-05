package com.devpaul.estructurapublicitarias_roal.domain.usecases.login

import com.devpaul.estructurapublicitarias_roal.data.models.PrincipalUser

sealed class LoginResult {
    data class Success(val data: PrincipalUser) : LoginResult()
    data class Error(val code: Int?, val title: String?, val subTitle: String?) : LoginResult()
    data object ValidationError : LoginResult()
}
